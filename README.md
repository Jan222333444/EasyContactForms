# EasyContactForms - A Backend for Contact Forms

Welcome to the readme file for the EasyContactForms program built with Spring Boot. This application allows you to store
or forward contact form submissions via a REST endpoint. Below you will find important information regarding the setup
and usage of the program.

## Functionality

The EasyContactForms Backend utilizes a REST endpoint to receive and process form submissions. The endpoint expects a
POST request with the necessary fields for the contact form entry. Once a request is received, it will either be stored
in a relational database or forwarded to the specified email address.

## Requirements

### Productive

- Java Version >= 17
- Gradle Version >= 7

### Development

- Java Version >= 17
- Gradle Version >= 7
- Docker

## Installation

Compile from source:

1. Ensure that you have Java and Gradle installed on your system.

2. Clone the [repository](https://github.com/Jan222333444/EasyContactForms).

3. Navigate to the project directory:

   ```
   cd EasyContactForms
   ```

4. Build the system:

   ```
   ./gradlew build
   ```

## Configuration

### Productive

1. Copy the file ````example.application.propterties```` into ``application.properties``

2. Configure the following properties according to your requirements:
   
   - Specify the database connection URL, username, and password for storing the form submissions.
   - Set the email settings to forward submissions to a particular email address.
      - You will need an email account from which this application will send the contact requests to your destination
        email 
3. Set up a proxy (e.g. nginx) to encrypt traffic

You can deactivate redirect to email by setting the configuration option ``redirect.mode=email`` to ``redirect.mode=none``

### Development

1. Open the ``dev.application.properties`` file located in the ``config`` directory.

2. If you want to use another database then H2 for development configure it here.

3. Copy the ``dev.application.properties`` to ``application.properties``

## Usage

1. Start the application:

   ```
   ./gradlew bootRun
   ```

2. The application will start on port 8080 by default. If you wish to change the port, update the "
   application.properties" file accordingly.

3. Send a POST request to the endpoint `http://localhost:8080/contact` with the required form data. Ensure that you
   include the correct fields and data format.

4. It is recommended to set up a proxy server to enable HTTPS. This is needed especially in context of the GDPR(General
   Data Protection Regulation) of the EU.

### Development

Additionally, you will need to start the smtp4dev server over Docker.

````bash
docker compose up -d
````


## Troubleshooting

- If you encounter any issues during installation or usage of the application, please check the error logs and console
  output for any error messages or warnings.

- Verify that all configuration parameters are set correctly, including the database connection and email settings.

## Plugin development

To develop a plugin implement the Plugin Interface from the ``PluginAPI`` subproject

You also need to implement the ``PluginFactory`` of this subproject and link it inside your implementation of the ``Plugin`` interface.

Last step is to add the file ``org.easycontactforms.api.Plugin`` with the full name of your Plugin implementation (including package path)
inside the ``resources/META-INF/services`` directory of your Gradle or Maven project.

An example is given in the ``TestPlugin`` subproject.

## Contribution

We welcome contributions to improve this Contact Form Backend program. If you have any suggestions for improvement or
have found any issues, please feel free to submit them in the form of pull requests or issues
on [https://github.com/Jan222333444/EasyContactForms](https://github.com/Jan222333444/EasyContactForms).

## License

This program is licensed under the MIT license and is freely available for personal and commercial use.

## Contact

For any questions or inquiries, please use the GitHub Issue system of this repository.

Thank you for using EasyContactForms!

© 2023, Jan Korb