$PC_IP = (Get-NetIPAddress -AddressFamily IPv4 | Where-Object {$_.InterfaceAlias -notmatch 'Loopback|vEthernet'}).IPAddress[0]
$PROXY_PORT = "8080"
$CERT_SOURCE = "$env:USERPROFILE\.mitmproxy\mitmproxy-ca-cert.pem"

Write-Host "1. Starting mitmweb..." -ForegroundColor Cyan
$wtCmd = "mitmweb -s mock-scripts\mock_spectator.py; adb shell settings put global http_proxy :0; Write-Host 'Proxy cleared.' -ForegroundColor Green"
$wtCmdEncoded = [Convert]::ToBase64String([Text.Encoding]::Unicode.GetBytes($wtCmd))
start wt -Args "-d .\ powershell -NoExit -EncodedCommand $wtCmdEncoded"
Start-Sleep -Seconds 3

Write-Host "2. Getting root access..." -ForegroundColor Cyan
adb root
Start-Sleep -Seconds 2

Write-Host "3. Computing cert hash and pushing certificate..." -ForegroundColor Cyan
$CERT_HASH = (& openssl x509 -inform PEM -subject_hash_old -in $CERT_SOURCE -noout) + ".0"
Write-Host "   Cert hash: $CERT_HASH" -ForegroundColor DarkCyan
adb push $CERT_SOURCE "/data/local/tmp/$CERT_HASH"

Write-Host "4. Installing trusted certificate into system store..." -ForegroundColor Cyan
adb shell "mount -t tmpfs tmpfs /system/etc/security/cacerts"
adb shell "cp /apex/com.android.conscrypt/cacerts/* /system/etc/security/cacerts/"
adb shell "cp /data/local/tmp/$CERT_HASH /system/etc/security/cacerts/"
adb shell "mount --bind /system/etc/security/cacerts /apex/com.android.conscrypt/cacerts"
adb shell "chmod 644 /system/etc/security/cacerts/*"
adb shell "chcon u:object_r:system_file:s0 /system/etc/security/cacerts/*"

Write-Host "5. Restarting Android runtime so Conscrypt reloads the cert store..." -ForegroundColor Cyan
adb shell stop
adb shell start
Write-Host "   Waiting for boot to complete..." -ForegroundColor DarkCyan
adb wait-for-device
do { Start-Sleep -Seconds 2; $booted = (adb shell getprop sys.boot_completed).Trim() } while ($booted -ne '1')
Start-Sleep -Seconds 2
Write-Host "   Boot complete." -ForegroundColor DarkGreen

Write-Host "6. Configuring proxy to $PC_IP`:$PROXY_PORT..." -ForegroundColor Cyan
adb shell "settings put global http_proxy $PC_IP`:$PROXY_PORT"
$confirmed = (adb shell "settings get global http_proxy").Trim()
if ($confirmed -ne "$PC_IP`:$PROXY_PORT") {
    Write-Host "   Retrying proxy configuration..." -ForegroundColor Yellow
    Start-Sleep -Seconds 3
    adb shell "settings put global http_proxy $PC_IP`:$PROXY_PORT"
}

Write-Host "--- All done ---" -ForegroundColor Green
Write-Host "Launch the app. Proxy will be cleared automatically when you close the mitmweb terminal."
