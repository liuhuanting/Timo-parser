# Timo-parser

Timo-parser 是一个脱胎于[Cobar] (https://github.com/alibaba/cobar)解析器的MySQL语法解析器。

它除了修复了一些原有的Bug，还增加了空间类型、大量MySQL函数、时间戳格式、以及各种MySQL数据类型的支持。

# usage

in `pom.xml`:

```
<repositories>
  <repository>
		<id>liuhuanting-maven-snapshot-repository</id>
		<name>liuhuanting-maven-snapshot-repository</name>
		<url>https://raw.github.com/liuhuanting/maven/snapshot/</url>
	</repository>
</repositories>

<dependencies>
	<dependency>
		<artifactId>timo-parser</artifactId>
		<groupId>com.github.liuhuanting</groupId>
		<version>1.0.0</version>
	</dependency>
</dependencies>
```
