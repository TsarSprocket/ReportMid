# Claude Code status line — ReportMid project (PowerShell)
# Receives JSON on stdin, outputs a formatted two-line status bar.

$raw = [Console]::In.ReadToEnd()
try   { $j = $raw | ConvertFrom-Json }
catch { [Console]::Out.WriteLine("status: bad JSON"); exit 1 }

# --- ANSI color codes ---
$ESC     = [char]27
$RST     = "$ESC[0m"
$BOLD    = "$ESC[1m"
$DIM     = "$ESC[2m"
$CYAN    = "$ESC[36m"
$GREEN   = "$ESC[32m"
$YELLOW  = "$ESC[33m"
$RED     = "$ESC[31m"
$BLUE    = "$ESC[34m"
$MAGENTA = "$ESC[35m"
$WHITE   = "$ESC[37m"

# Safe deep property read; returns '' for missing/null values
function Get-Prop([object]$obj, [string[]]$path) {
    $cur = $obj
    foreach ($key in $path) {
        if ($null -eq $cur) { return '' }
        $p = $cur.PSObject.Properties[$key]
        if ($null -eq $p) { return '' }
        $cur = $p.Value
    }
    if ($null -eq $cur) { return '' }
    return [string]$cur
}

# --- Extract fields ---
$cwd          = Get-Prop $j 'cwd'
$model        = Get-Prop $j 'model','display_name'
$used_pct     = Get-Prop $j 'context_window','used_percentage'
$effort       = Get-Prop $j 'effort','level'
$thinking     = Get-Prop $j 'thinking','enabled'      # "True" / "False" / ""
$git_owner    = Get-Prop $j 'workspace','repo','owner'
$git_repo_nm  = Get-Prop $j 'workspace','repo','name'
$git_worktree = Get-Prop $j 'workspace','git_worktree'
$session_name = Get-Prop $j 'session_name'
$pr_number    = Get-Prop $j 'pr','number'
$pr_state     = Get-Prop $j 'pr','review_state'
$five_hr      = Get-Prop $j 'rate_limits','five_hour','used_percentage'
$seven_day    = Get-Prop $j 'rate_limits','seven_day','used_percentage'
$wt_branch    = Get-Prop $j 'worktree','branch'

# --- Directory: backslashes -> forward slashes, shorten home to ~ ---
$display_dir = $cwd -replace '\\', '/'
if ($display_dir -and $env:USERPROFILE) {
    $home_fwd = $env:USERPROFILE -replace '\\', '/'
    if ($display_dir.StartsWith($home_fwd, [System.StringComparison]::OrdinalIgnoreCase)) {
        $display_dir = '~' + $display_dir.Substring($home_fwd.Length)
    }
}

# --- Git branch ---
$git_branch = ''
if ($wt_branch) {
    $git_branch = $wt_branch
} elseif ($git_worktree) {
    $git_branch = $git_worktree
} elseif ($cwd) {
    $git_branch = (& git -C $cwd symbolic-ref --short HEAD 2>$null) -join ''
    if (-not $git_branch) {
        $git_branch = (& git -C $cwd rev-parse --short HEAD 2>$null) -join ''
    }
}

# --- Context usage bar (10 chars wide) ---
$ctx_segment = ''
if ($used_pct) {
    $pct       = [double]$used_pct
    $filled    = [Math]::Min([int][Math]::Floor($pct / 10 + 0.5), 10)
    $empty_cnt = 10 - $filled
    $bar_fill  = '#' * $filled
    $bar_empty = '.' * $empty_cnt
    $bar_color = if ($pct -gt 85) { $RED } elseif ($pct -gt 60) { $YELLOW } else { $GREEN }
    $pct_str   = [int][Math]::Round($pct)
    $ctx_segment = "${bar_color}[${bar_fill}${bar_empty}] ${pct_str}%${RST}"
}

# ====================================================================
# LINE 1 : directory  (repo:branch)  PR#  [session]
# ====================================================================
$line1 = ''

if ($display_dir) { $line1 = "${CYAN}${BOLD}${display_dir}${RST}" }

if ($git_owner -or $git_repo_nm -or $git_branch) {
    $git_seg = ''
    if ($git_owner -and $git_repo_nm) {
        $git_seg = "${BLUE}${git_owner}/${git_repo_nm}${RST}"
    } elseif ($git_repo_nm) {
        $git_seg = "${BLUE}${git_repo_nm}${RST}"
    }
    if ($git_branch) {
        if ($git_seg) { $git_seg += "${DIM}:${RST}" }
        $git_seg += "${MAGENTA}${git_branch}${RST}"
    }
    if ($line1) { $line1 += '  ' }
    $line1 += "${DIM}(${RST}${git_seg}${DIM})${RST}"
}

if ($pr_number) {
    $pr_color = switch ($pr_state) {
        'approved'          { $GREEN  }
        'changes_requested' { $RED    }
        'draft'             { $DIM    }
        default             { $YELLOW }
    }
    if ($line1) { $line1 += '  ' }
    $line1 += "${pr_color}PR#${pr_number}${RST}"
}

if ($session_name) {
    if ($line1) { $line1 += '  ' }
    $line1 += "${DIM}[${session_name}]${RST}"
}

# ====================================================================
# LINE 2 : model  effort:level  thinking  ctx:[####......] N%  limits
# ====================================================================
$line2 = ''

if ($model) { $line2 = "${WHITE}${model}${RST}" }

if ($effort) {
    $ef_color = switch ($effort) {
        'low'    { $DIM              }
        'medium' { $WHITE            }
        'high'   { $YELLOW           }
        'xhigh'  { $RED              }
        'max'    { "${RED}${BOLD}"   }
        default  { $WHITE            }
    }
    if ($line2) { $line2 += '  ' }
    $line2 += "${ef_color}effort:${effort}${RST}"
}

if ($thinking -eq 'True') {
    if ($line2) { $line2 += '  ' }
    $line2 += "${MAGENTA}thinking${RST}"
}

if ($ctx_segment) {
    if ($line2) { $line2 += '  ' }
    $line2 += "${DIM}ctx:${RST}${ctx_segment}"
}

$rate_seg = ''
if ($five_hr) {
    $rate_seg = "${YELLOW}5h:$([int][Math]::Round([double]$five_hr))%${RST}"
}
if ($seven_day) {
    if ($rate_seg) { $rate_seg += ' ' }
    $rate_seg += "${YELLOW}7d:$([int][Math]::Round([double]$seven_day))%${RST}"
}
if ($rate_seg) {
    if ($line2) { $line2 += '  ' }
    $line2 += "${DIM}limits:${RST}${rate_seg}"
}

# ====================================================================
# Output
# ====================================================================
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::Out.WriteLine($line1)
[Console]::Out.WriteLine($line2)
