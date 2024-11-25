pipeline{
    agent any

    tools {
        maven 'maven3'
    }

    stages{
        stage("Build"){
            steps{
               sh 'mvn clean package'
            }
        }

        stage("Checkstyle Analysis"){
            steps{
               sh 'mvn checkstyle:checkstyle'
            }
        }

        stage("sonar Analysis"){
            environment {
                scannerHome = tool 'sonar6.2'
            }
            steps{
                script {
                    withSonarQubeEnv('sonar') {
                                    sh '''${scannerHome}/bin/sonar-scanner \
                                            -Dsonar.projectKey=employee \
                                            -Dsonar.projectName=employee \
                                            -Dsonar.projectVersion=1.0 \
                                            -Dsonar.sources=src/ \
                                            -Dsonar.host.url=http://54.172.159.143/ \
                                            -Dsonar.java.binaries=target/test-classes/com/employees/employeemanager/ \
                                            -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml

                                        '''
                                    }
                }

                }

            }

        stage("Quality Gate"){
            steps{
               timeout(time: 1, unit: 'HOURS'){
                    waitForQualityGate abortPipeline: true
                }

            }
        }

        stage("Upload Artifacts"){
            steps{
                    nexusArtifactUploader(
                                nexusVersion: 'nexus3',
                                protocol: 'http',
                                nexusUrl: '172.48.16.120:8081',
                                groupId: 'QA',
                                version: "${BUILD_ID}",
                                repository: 'employee-repo',
                                credentialsId: 'nexus',
                                artifacts: [
                                        [artifactId: employeemgmt,
                                        classifier: '',
                                        file: 'target/employeemanager-0.0.1-SNAPSHOT.jar',
                                        type: 'jar']
                                    ]
                )
            }
        }




        }
    }

