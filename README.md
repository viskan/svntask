# svntask

This is a fork of [opticyclic/svntask](https://github.com/opticyclic/svntask) which in turn is a fork of [svntask](http://code.google.com/p/svntask/) together with some commands from [chripo/svntask](https://github.com/chripo/svntask)

It is a simple wrapper around [svnkit](http://svnkit.com/)


## Available commands

 - add
 - checkout
 - commit
 - diff
 - export
 - info
 - log
 - ls
 - status
 - switch
 - update
 - cleanup
 - copy
 - delete
 - mkdir
 - unlock


## Usage

### Maven

`pom.xml`:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-antrun-plugin</artifactId>
    <version>${maven-antrun-plugin.version}</version>
    <executions>
        <execution>
            <id>generate-patch</id>
            <phase>generate-sources</phase>
            <goals>
                <goal>run</goal>
            </goals>
            <configuration>
                <target>
                    <taskdef resource="svntask.xml" classpathref="maven.plugin.classpath"/>
                    <svn username="${tomcat.username}" password="${tomcat.password}">
                        <diff workingcopy="/path/to/workingcopy" outfilename="svn.patch" />
                    </svn>
                </target>
            </configuration>
        </execution>
    </executions>
    <dependencies>
        <dependency>
            <groupId>com.viskan</groupId>
            <artifactId>svntask-1.8</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
</plugin>
```


### Authentication

```xml
    <svn username="user" password="password">
        <!-- Commands -->
    </svn>
```


### Commands

#### cleanup

```xml
<svn>
    <cleanup path="${project.src}" deleteWCProperties="false" />
</svn>
```


#### copy

```xml
<svn>
    <copy
        failOnDstExists="true"
        move="false"
        src="${from.file / from.url}"
        dst="${to.file / to.url}"
        commitMessage="copy by svntask" />
    </svn>
```


# delete
      <svn>
        <delete
          force="true"
          deleteFiles="false"
          includeDirs="true"
          dryRun="false"
          path="/path/to/delete"
        />
    
        <delete
          force="true"
          deleteFiles="false"
          includeDirs="true"
          dryRun="false">
          <fileset>
            ...
          </fileset>
        </delete>
      </svn>


# export
      <target name="export">
        <svn>
          <export workingcopy="/path/to/workingcopy"
                  exportpath="/path/to/exportdir"/>
        </svn>
        <svn username="guest" password="">
          <export url="http://host/svn/repo"
                  exportpath="/path/to/exportdir"/>
        </svn>
      </target>


# diff
      <target name="diff">
        <svn username="guest" password="">
          <diff workingcopy="/path/to/workingcopy"
                  outfilename="svn.patch"/>
        </svn>
      </target>


# mkdir
      <svn>
        <mkdir
          makeParents="true"
          path="path/to/create"
          commitMessage="mkdir by svntask"
        />
      </svn>


# unlock
      <svn>
        <unlock
          breakLock="true"
          includeDirs="true">
            <fileset>
              ...
            </fileset>
        </unlock>
      </svn>
