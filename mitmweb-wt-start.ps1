$CERT_HASH = "c8750f0d.0"

$PC_IP = (Get-NetIPAddress -AddressFamily IPv4 | Where-Object {$_.InterfaceAlias -notmatch 'Loopback|vEthernet'}).IPAddress[0]
$PROXY_PORT = "8080"

Write-Host "1. Запуск mitmweb в новом окне терминала..." -ForegroundColor Cyan
start wt -Args "-d .\ powershell -NoExit -Command mitmweb -s mock-scripts\mock_spectator.py"

Write-Host "2. Подготовка эмулятора (Root)..." -ForegroundColor Cyan
adb root
Start-Sleep -s 2

Write-Host "3. Применение патча сертификата (Android 14)..." -ForegroundColor Cyan
# Создаем временную папку и объединяем сертификаты
adb shell "mkdir -p /data/local/tmp/cacerts"
adb shell "mount -t tmpfs tmpfs /system/etc/security/cacerts"
adb shell "cp /apex/com.android.conscrypt/cacerts/* /system/etc/security/cacerts/"
adb shell "cp /data/misc/user/0/cacerts-added/$CERT_HASH /system/etc/security/cacerts/"
adb shell "mount --bind /system/etc/security/cacerts /apex/com.android.conscrypt/cacerts"
adb shell "chmod 644 /system/etc/security/cacerts/*"
adb shell "chcon u:object_r:system_file:s0 /system/etc/security/cacerts/*"

Write-Host "4. Установка системного прокси на $PC_IP`:$PROXY_PORT..." -ForegroundColor Cyan
adb shell "settings put global http_proxy $PC_IP`:$PROXY_PORT"

Write-Host "--- ВСЁ ГОТОВО ---" -ForegroundColor Green
Write-Host "Теперь весь трафик эмулятора идет через mitmweb."
Write-Host "Чтобы сбросить прокси в будущем, используйте: adb shell settings put global http_proxy :0"