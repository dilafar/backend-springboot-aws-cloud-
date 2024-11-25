pipeline{
    agent any

    tools {
        maven 'maven3'
    }

    stages{
        stage("Fetch Code"){
            steps{
               git branch: 'main', url: 'https://github.com/dilafar/backend-springboot-aws-cloud-.git'
            }
        }

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
                scannerHome = tool 'sonar4.7'
            }
            steps{
               withSonarQubeEnv('sonar') {
                    sh '''${scannerHome}/bin/sonar-scanner \
                                    -Dsonar.projectKey=banking \
                                    -Dsonar.projectName=banking-microservice-repo \
                                    -Dsonar.projectVersion=1.0 \
                                    -Dsonar.sources=src/ \
                                    -Dsonar.java.binaries=target/classes \
                                    -Dsonar.jacoco.reportsPath=target/jacoco.exec \
                                    -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml
                        '''
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
                                nexusUrl: '172.16.12.32:8080',
                                groupId: 'QA',
                                version: "${BUILD_ID}-${BUILD_TIMESTAMP}",
                                repository: 'bank-microservice',
                                credentialsId: 'nexuslogin',
                                artifacts: [
                                        [artifactId: microserviceapp,
                                        classifier: '',
                                        file: 'accounts' + version + '.jar',
                                        type: 'jar']
                                    ]
                )
            }
        }




        }
    }

