# Resources Folder

This just contains resources that may be useful for testing/development.

For example [redirect-to-localhost.sh][redirect] and [remove-redirect-to-localhost.sh][no-redirect] are useful for redirecting the official Among Us server ips to localhost. [regionInfo.dat][regionFile] is for changing which server the official Among Us client connects to (if you can replace the file on your platform). Also, [example-plugin.jar][examplePlugin] is a test plugin that is used to verify the mime type plugin detector works.

To build the example plugin from scratch, run the command `./gradlew buildExamplePlugin`.

[redirect]: redirect-to-localhost.sh
[no-redirect]: remove-redirect-to-localhost.sh
[regionFile]: regionInfo.dat
[examplePlugin]: example-plugin.jar