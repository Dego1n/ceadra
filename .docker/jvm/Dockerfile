FROM adoptopenjdk:14.0.2_12-jdk-openj9-0.21.0

ENV TZ=Europe/Moscow
RUN  ln -sf /usr/share/zoneinfo/$TZ /etc/timezone && \
     ln -sf /usr/share/zoneinfo/$TZ /etc/localtime

RUN date

RUN curl -L https://services.gradle.org/distributions/gradle-6.8-bin.zip -o gradle-6.8-bin.zip
RUN apt -o Acquire::Max-FutureTime=86400 update
RUN apt -y install unzip
RUN unzip gradle-6.8-bin.zip
ENV GRADLE_HOME=/gradle-6.8
ENV PATH=$PATH:$GRADLE_HOME/bin

WORKDIR /jvm
