# RasterMaster Sample Maven Project

This sample project will convert all pages from the input file and output them as `PNG`. You must provide a valid `slicense.json` to use this library or you will receive watermarks on every page converted.



# Getting Started Quickly

1. Clone this git repository to your local machine.
2. Import it as a Maven project into your favorite IDE.
3. Set the program arguments to `<input file path>` and `<path to license>`.
   - If your path has spaces put the path in quotation marks `"c:/Users/Dan P/Documents/test.pdf"` .
4. Click `Debug`.



# Getting Started Without an IDE

Using Maven only, you can compile and run this test app in a command prompt using the following command

`mvn compile exec:java -Dexec.mainClass="com.snowbound.re.maven_examples.conversion.Main" -Dexec.arguments="SomePDF.pdf,SnowboundLicense.jar"`



# Adding to an existing Maven project

If you already have a Maven project, follow these three steps.



1. Add this to the `<project> ` section of your `pom.xml`.
   - You may already have a repositories section. If that is the case, only copy the `<repository>` block and merge it into the existing list of `<repositories>`.

```
<repositories>
	<!-- This repository is listed here so we can fetch Snowbound artifacts -->
	<repository>
		<id>snowbound-public</id>
		<name>Snowbound Public Repository</name>
		<url>https://repo.snowbound.com/repository/snowbound-public/</url>
		<releases>
			<enabled>true</enabled>
		</releases>
		<snapshots>
			<enabled>false</enabled>
		</snapshots>
	</repository>
	
	<!-- Not all required iText artifacts are on mvnrepository.com -->
	<repository>
		<id>itext-public</id>
		<name>iText Public Repository</name>
		<url>https://repo.itextsupport.com/releases/</url>
		<releases>
			<enabled>true</enabled>
		</releases>
		<snapshots>
			<enabled>false</enabled>
		</snapshots>
	</repository>
</repositories>
```



2. Add this to the `<dependencies>` section of your `pom.xml`.

```
<!-- Snowbound RasterMaster (https://repo.snowbound.com/) -->
<dependency>
	<groupId>com.snowbound.rastermaster.java</groupId>
	<artifactId>rastermaster</artifactId>
	<version>20.6.0</version>
</dependency>
```



3. Refresh your Maven project.