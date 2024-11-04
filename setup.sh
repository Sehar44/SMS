#!/bin/bash

#Update package list and install required packages
apt-get update
apt-get install -y openjdk-17-jdk maven

#Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
