#!/bin/sh
input=$(cat)

cwd=$(echo "$input" | jq -r '.cwd // .workspace.current_dir // empty')
model=$(echo "$input" | jq -r '.model.display_name // empty')
used=$(echo "$input" | jq -r '.context_window.used_percentage // empty')

# Get git branch from cwd, skipping optional locks
branch=""
if [ -n "$cwd" ]; then
  branch=$(git -C "$cwd" --no-optional-locks symbolic-ref --short HEAD 2>/dev/null)
fi

# Build status line parts
dir_part=""
[ -n "$cwd" ] && dir_part=$(printf '\033[34m%s\033[0m' "$cwd")

branch_part=""
[ -n "$branch" ] && branch_part=$(printf '\033[32m(%s)\033[0m' "$branch")

model_part=""
[ -n "$model" ] && model_part=$(printf '\033[36m%s\033[0m' "$model")

ctx_part=""
if [ -n "$used" ]; then
  ctx_int=$(printf '%.0f' "$used")
  ctx_part=$(printf '\033[33mctx:%s%%\033[0m' "$ctx_int")
fi

# Assemble line, joining non-empty parts with spaces
line=""
for part in "$dir_part" "$branch_part" "$model_part" "$ctx_part"; do
  [ -z "$part" ] && continue
  if [ -z "$line" ]; then
    line="$part"
  else
    line="$line $part"
  fi
done

printf '%b\n' "$line"
