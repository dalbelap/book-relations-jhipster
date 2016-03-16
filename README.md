# Relation References

A database text to store and find resources. It stores a date (string values), code, autor, title, url and two images. Also relation each resource with the autenticated user. Only de current user can view and edit its resources.

Relation References application web was generated with JHipster using PostgreSQL database, you can find documentation and help at [JHipster][].


Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools (like
[Bower][] and [BrowserSync][]). You will only need to run this command when dependencies change in package.json.

    npm install

We use [Grunt][] as our build system. Install the grunt command-line tool globally with:

    npm install -g grunt-cli

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    mvn
    grunt

Bower is used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in `bower.json`. You can also run `bower update` and `bower install` to manage dependencies.
Add the `-h` flag on any command to see how you can use it. For example, `bower update -h`.

# Building for production

To optimize the relaciones client for production, run:

    mvn -Pprod clean package

This will concatenate and minify CSS and JavaScript files. It will also modify `index.html` so it references
these new files.

To ensure everything worked, run:

    java -jar target/*.war --spring.profiles.active=prod

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

# Testing

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in `src/test/javascript` and can be run with:

    grunt test

# Continuous Integration

To setup this project in Jenkins, use the following configuration:

* Project name: `relaciones`
* Source Code Management
    * Git Repository: `git@github.com:xxxx/relaciones.git`
    * Branches to build: `*/master`
    * Additional Behaviours: `Wipe out repository & force clone`
* Build Triggers
    * Poll SCM / Schedule: `H/5 * * * *`
* Build
    * Invoke Maven / Tasks: `-Pprod clean package`
* Post-build Actions
    * Publish JUnit test result report / Test Report XMLs: `build/test-results/*.xml`

[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Grunt]: http://gruntjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/

YAML Configuration
------------------

- Create *src/main/resources/config/application.yml* file with the below data:
```
spring:
    jpa:
        open-in-view: false
        hibernate:
            ddl-auto: none
            naming-strategy: org.springframework.boot.orm.jpa.hibernate.SpringNamingStrategy
    messages:
        basename: classpath:/i18n/messages
    mvc:
        favicon:
            enabled: false
    thymeleaf:
        mode: XHTML

security:
    basic:
        enabled: false

# ===================================================================
# JHipster specific properties
# ===================================================================

jhipster:
    async:
        corePoolSize: 2
        maxPoolSize: 50
        queueCapacity: 10000
    mail:
        from: relaciones@localhost
    security:
        authentication:
            oauth:
                clientid: relacionesappId # Change this value
                secret: secretKey # Change this value
                # Token is valid 30 minutes
                tokenValidityInSeconds: 1800
        rememberme:
            # security key (this key should be unique for your application, and kept secret)
            key: `cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1 | sha256sum | cut -d' ' -f 1` # Change this value as random
    swagger:
        title: Book relations API
        description: Book relations API documentation
        version: 0.0.1
        termsOfServiceUrl:
        contact:
        license:
        licenseUrl:

```

- Create .yo-rc.json configuration file
```
{
  "generator-jhipster": {
    "baseName": "relaciones",
    "packageName": "io.github.relaciones",
    "packageFolder": "io/github/relaciones",
    "authenticationType": "oauth2",
    "hibernateCache": "ehcache",
    "clusteredHttpSession": "no",
    "websocket": "no",
    "databaseType": "sql",
    "devDatabaseType": "postgresql",
    "prodDatabaseType": "postgresql",
    "searchEngine": "elasticsearch",
    "useSass": false,
    "buildTool": "maven",
    "frontendBuilder": "grunt",
    "enableTranslation": true,
    "rememberMeKey": "`cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1 | sha256sum | cut -d' ' -f 1`"
  }
}
```

- Email configuration: pre-configured for a [Mailgun](https://mailgun.com) service:

```
$ vim src/main/resources/config/application-prod.yml
mail:
    host: smtp.mailgun.org
    port: 465
    username: postmaster@yourdomain-from-mailgun-servie.mailgun.org
    password: yourpassword-from-your-mailgun-service
    protocol: smtps
    tls: true
    auth: true
    from: no-reply@localhost
```

Run
---

- Developer mode
```
$ mvn compile
$ mvn -Dmaven.test.skip=true spring-boot:run
```

Execute in other terminal grunt task serve:
```
$ grunt serve
```

- Production mode
```
$ mvn -Pprod -Dmaven.test.skip=true package
$ java -jar ./target/flipper-0.0.1-SNAPSHOT.war --spring.profiles.active=prod
```

Deploy in OpenShipt
-------------------
Classic deployment with 'yo jhipster:openshift' won't start for small gears because of the limited quota disk space. To resolve this issue on a small gear use a Tomcat 7 application with Java8 instead:
0. Clone and compile this repo
```
$ git clone []
$ cd []
$ mvn -Pprod -Dmaven.test.skip=true package
$ ls target/*.war*
```
1. Install and configure rhc with your account from [OpenShipt](openshift.redhat.com).
2. Create a new applicacion with rhc
```
$ rhc app create [appName] jbossews-2.0
$ rhc cartridge add postgresql-9.2 -a [appName]
```
3. Set production to JAVA_OPTS enviroment
```
$ rhc env set JAVA_OPTS="-Dspring.profiles.active=prod" --app [appName]
```

4. Clone and configure deployment app
```
$ git clone ssh://[id-host]@[appName]-[domain].rhcloud.com/~/git/[appName].git/
$ cd [appName]
# Delete unnecessary files
$ git rm pom.xml src/ -r
# Enable Java8
$ git mv .openshift/markers/java7 .openshift/markers/java8
```
5. Add war.original file
```
$ cp ../cloned-app/target/cloned-app-0.0.1-SNAPSHOT.war.original webapps/ROOT.war
$ git add .
$ git commit -am 'added root war'
$ git push
```

6. View state from app
```
$ rhc app show --state [appName]
```

7. View logs
```
$ rhc tail -a [appName]
```
License
-------
Relation References
Copyright (C) 2015  D. Albela

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

More info in "COPYING" file
