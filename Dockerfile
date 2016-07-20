FROM devlgiglrg04.dstcorp.net:5000/rhel6:latest
MAINTAINER Apratim Mishra<amishra@dstsystems.com>
# Handle any proxy configurations that need to be made
ADD toggle_proxy.sh /usr/bin/toggle_proxy.sh
RUN chmod +x /usr/bin/toggle_proxy.sh && echo ". /usr/bin/toggle_proxy.sh" | tee -a ~/.shrc ~/.bashrc
RUN source ~/.shrc && yum -y update && yum -y install tar wget java-1.7.0-openjdk
# Install Java.
WORKDIR /opt
RUN source ~/.shrc && wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u31-b13/jdk-8u31-linux-x64.tar.gz"
RUN  tar xzf jdk-8u31-linux-x64.tar.gz
RUN  alternatives --install /usr/bin/java java /opt/jdk1.8.0_31/bin/java 1
RUN  alternatives --install /usr/bin/jar jar /opt/jdk1.8.0_31/bin/jar 1
RUN  alternatives --install /usr/bin/javac javac /opt/jdk1.8.0_31/bin/javac 1
RUN  alternatives --set jar /opt/jdk1.8.0_31/bin/jar
RUN  alternatives --set javac /opt/jdk1.8.0_31/bin/javac
RUN  echo "export JAVA_HOME=/opt/jdk1.8.0_31" | tee -a ~/.bashrc ~/.shrc
RUN  echo "export JRE_HOME=/opt/jdk1.8.0_31/jre" | tee -a ~/.bashrc ~/.shrc
RUN  echo "export PATH=$PATH:/opt/jdk1.8.0_31/bin:/opt/jdk1.8.0_31/jre/bin" | tee -a ~/.bashrc ~/.shrc
RUN  echo -e "apple12\napple12" | (passwd --stdin root)

EXPOSE 8080
# Define default command.
ENTRYPOINT ["sbt"]
CMD ["container:start"]
