:: This list may not be complete, please send me more ip addresses accessed when this redirect is on
:: You NEED Admin privileges to be able to run this file
@echo off

for /f "skip=3 tokens=4,*" %%a in ('
    netsh interface ipv4 show interface
') do (
    echo +Found adapter %%b
    if "%%a"=="connected" (
        echo   +Enable dhcp/staticip-coexistence
        :: Enable dhcp/staticip-coexistence, otherwise dhcp would be disabled
        netsh interface ipv4 set interface "%%b" dhcpstaticipcoexistence=enabled
        
        :: Masters Lists Hardcoded
        netsh interface ipv4 add address "%%b" 66.175.220.120  255.255.255.255
        netsh interface ipv4 add address "%%b" 45.79.40.75     255.255.255.255
        netsh interface ipv4 add address "%%b" 104.237.135.186 255.255.255.255
        
        :: America
        netsh interface ipv4 add address "%%b" 198.58.115.57   255.255.255.255
        netsh interface ipv4 add address "%%b" 198.58.99.71    255.255.255.255
        netsh interface ipv4 add address "%%b" 50.116.1.42     255.255.255.255
        netsh interface ipv4 add address "%%b" 45.79.5.6       255.255.255.255
        netsh interface ipv4 add address "%%b" 45.79.67.124    255.255.255.255
        
        :: Asia
        netsh interface ipv4 add address "%%b" 172.104.96.99   255.255.255.255
        netsh interface ipv4 add address "%%b" 139.162.111.196 255.255.255.255
        
        :: Europe
        netsh interface ipv4 add address "%%b" 139.162.220.199 255.255.255.255
        netsh interface ipv4 add address "%%b" 172.105.251.35  255.255.255.255
        netsh interface ipv4 add address "%%b" 172.105.251.170 255.255.255.255
        netsh interface ipv4 add address "%%b" 172.105.249.25  255.255.255.255
    )
)
echo Finished!
