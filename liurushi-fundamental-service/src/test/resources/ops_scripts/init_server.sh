#!/bin/bash
# @Author konglong
# @Created at 2015.07.30

if [ $# -le 0 ]; then
  echo "Usage: sh init_server.sh <user name>"
  # Incorrect usage
  exit 2
fi

sudoers_file="/etc/sudoers"

createGroupAndUser() {
  echo "beginning create group(devops) and user($1)"
  groupadd devops
  useradd -g devops $1
  echo "5!xiAoHuang" | passwd --stdin $1
}

addUserToSudoers() {
  grep "$1" ${sudoers_file} > /dev/null
  if [ $? -ne 0 ]; then
    echo "User $1 doesn't have sudo permission!"
    echo "$1 ALL=(ALL) NOPASSWD:ALL,!/bin/su" >> ${sudoers_file} && echo "Add user $1 to sudoers successfully!"
  else
    echo "User $1 already has sudo permission!"
  fi
}

forbidRootToLogin() {
  sed -i '/^#PermitRootLogin yes$/a\PermitRootLogin no' /etc/ssh/sshd_config
  service sshd restart
  service sshd status
  echo "Forbid root to login successfully!"
}

switchYumRepoToAliyun() {
  echo "Refer to http://mirrors.aliyun.com/help/centos"
}

createGroupAndUser "$1"
addUserToSudoers "$1"
#forbidRootToLogin
