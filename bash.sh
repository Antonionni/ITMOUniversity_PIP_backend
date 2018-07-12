#!/bin/bash
prjName='play-authenticate-hibernate'
prjVersion='1.0-SNAPSHOT'
packageSrcFolder='./target/universal'
prjFullName=$prjName'-'$prjVersion
packagePath=$packageSrcFolder'/'$prjFullName
packageZip=$packagePath'.zip'
packageHackedRunner=$packageSrcFolder'/play-authenticate-hibernate'
packageProdPath='~/'$prjFullName

set -x
rm -rf ~/play/$prjFullName
unzip -qo $packageZip -d ~/play

rsync -ru --delete --rsync-path=/opt/csw/bin/rsync -e "ssh -p2222" ~/play/$prjFullName/ s172233@se.ifmo.ru:~/$prjFullName
scp -P2222 $packageHackedRunner s172233@se.ifmo.ru:$packageProdPath/bin/$prjName
ssh -p2222 s172233@se.ifmo.ru << EOF
export PATH="~/bin/:$PATH"
which java
cd $packageProdPath
kill -s 9 `cat ./RUNNING_PID`
rm -f ./RUNNING_PID
./bin/$prjName -DsocksProxyHost=5.188.232.39 -DsocksProxyPort=443 -DsocksNonProxyHosts=pg\|localhost -Dhttp.port=1238 -Dhttp.address=127.0.0.1
EOF
