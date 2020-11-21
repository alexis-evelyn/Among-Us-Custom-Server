:: This list may not be complete, please send me more ip addresses accessed when this redirect is on
:: You NEED Admin privileges to be able to run this file
@echo off

for /f "skip=3 tokens=4,*" %%a in ('
    netsh interface ipv4 show interface
') do (
    echo +Found adapter %%b
    if "%%a"=="connected" (
        echo   +Disable dhcp/staticip-coexistence
        :: Disable dhcp/staticip-coexistence again (maybe this is unnecessary or even undesired)
        netsh interface ipv4 set interface "%%b" dhcpstaticipcoexistence=disabled
        
        :: Masters Lists Hardcoded
        netsh interface ipv4 delete address "%%b" 66.175.220.120
        netsh interface ipv4 delete address "%%b" 45.79.40.75
        netsh interface ipv4 delete address "%%b" 104.237.135.186
        
        :: America
        netsh interface ipv4 delete address "%%b" 198.58.115.57
        netsh interface ipv4 delete address "%%b" 198.58.99.71
        netsh interface ipv4 delete address "%%b" 50.116.1.42
        netsh interface ipv4 delete address "%%b" 45.79.5.6
        netsh interface ipv4 delete address "%%b" 45.79.67.124
        
        :: Asia
        netsh interface ipv4 delete address "%%b" 172.104.96.99
        netsh interface ipv4 delete address "%%b" 139.162.111.196
        
        :: Europe
        netsh interface ipv4 delete address "%%b" 139.162.220.199
        netsh interface ipv4 delete address "%%b" 172.105.251.35
        netsh interface ipv4 delete address "%%b" 172.105.251.170
        netsh interface ipv4 delete address "%%b" 172.105.249.25
    )
)
echo Finished!
