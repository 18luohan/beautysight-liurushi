#!/bin/bash
# @Author konglong
# @Created at 2015.08.20

# Set centos repo to aliyun yum repo: http://mirrors.aliyun.com/help/centos
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup \
        && curl -o /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo \
        && yum makecache

echo "Switch to aliyun repo successfully!"