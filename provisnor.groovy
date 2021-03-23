import groovy.json.JsonSlurper
hudson.FilePath workspace = hudson.model.Executor.currentExecutor().getCurrentWorkspace()

def configurationinputs = new JsonSlurper().parseText(new File("${workspace}/pipelineinput.json").text)
configurationinputs['buildinputs'].each{ buildinput ->
    
    
    mainjobname="${buildinput['jobname']}_MainJob"
    jmeterjobname="${buildinput['jobname']}_Jmeter"
    seleniumjobname="${buildinput['jobname']}_SeleniumAPI"
    jmeterdownstream="${buildinput['jobname']}_Jmeter_Downstream"

    String maven_version="${buildinput['maven_version']}"
    createmainjob(mainjobname,seleniumjobname,jmeterjobname)
    createseleniumjob(seleniumjobname,maven_version)
    createjmeterjob(jmeterjobname,maven_version)    
}
def createmainjob(String mainjobname,String seleniumjobname,String jmeterjobname)
{
    multiJob('APTF_2.0_MainJob'){
        parameters {            
            stringParam( 'buildNumber',  '${BUILD_NUMBER}',  'Jenkins BuildNo')
            stringParam( 'NoOfThreads',  '6',  'Number of Users')
            stringParam( 'RampUp',  '10',  'RampUp duration (in seconds)')
            stringParam( 'duration',  '300',  'Duration(in Sec)')
            stringParam( 'TestName',  'Sample',  'Enter Test Name')
            stringParam( 'JobName',  '${JOB_NAME}',  'Jenkins Job Name')
            stringParam( 'InfluxDBIPAddress',  '172.24.112.219',  'Enter InfluxDB Host IP Address')
            stringParam( 'SeleniumDBName',  'APTF_SeleniumAPI',  'Selenium InfluxDB Name')
            stringParam( 'SeleniumScriptName',  'SeleniumIntranetScript',  'SeleniumScript to Run')
            stringParam( 'ExcelOutputPath',  'C:\\APTF_2.0_SeleniumAPIJMeter\\',  'Selenium Excel Output Path(Use Double Slash)')
            stringParam( 'WebDriverPath',  'C:\\APTF_2.0_SeleniumAPIJMeter\\Drivers\\',  'Selenium Web Driver Path(Use Double Slash)')
            stringParam( 'InfluxDBUrl',  'http://172.24.112.219:8086',  'Influx DB Url')
            stringParam( 'ScenarioLevelDashboardLink',  'https://perf-grafana.aspiresys.com/d/dNgpidVMz/scenariolevelmetrics?orgId=1',  'Jmeter Scenario Level Dashboard')
            stringParam( 'BuildHistoryDashboardLink',  'https://perf-grafana.aspiresys.com/d/6KIVmd4Gz/buildhistory?orgId=1',  'Build History Dashboard Link')
            stringParam( 'ComparisonDashboardLink',  'https://perf-grafana.aspiresys.com/d/5YCVmO4Mz/comparison?orgId=1',  'Comparison Dashnboard Link')
            stringParam( 'ResposeCodeWiki',  'file://aspirevm15-18/ResposeCodeDetailsHTML3/HTTP%20Status%20Code%20Document%20V1.1.htm',  'Response Code Status')
            stringParam( 'JmeterIndividualDashboard',  'https://perf-grafana.aspiresys.com/d/_7iviOVGk/individualstepmetrics?orgId=1',  'Jmeter Individual Dashboard Link')
            stringParam( 'SeleniumIndividualDashboard',  'file:https://perf-grafana.aspiresys.com/d/N30omd4Mk/selenium_individual?orgId=1',  'Selenium Individual Dashboard Link')
            stringParam( 'RunhomePageDashboard',  'file:https://perf-grafana.aspiresys.com/d/5qV5idVGz/runhomepage?orgId=1',  'Selenium RunHomePage Dashboard Link')
            stringParam( 'HomePageDashboard',  'https://perf-grafana.aspiresys.com/d/MF7HmdVGz/homepage?orgId=1',  'Home Page Dashboard Link')
            stringParam( 'StepLevelDashboard',  'https://perf-grafana.aspiresys.com/d/77L2iOVGk/steplevelmetrics?orgId=1',  'Step Level Dashboard Link')
            stringParam( 'RequestLevelDashboard',  'https://perf-grafana.aspiresys.com/d/Kd2Fmd4Gz/requestlevelmetrics?orgId=1',  'Request Level Dashboard Link')
            stringParam( 'LoadGeneratorPerformance',  'https://perf-grafana.aspiresys.com/d/mysDid4Gk/loadgeneratorperformance?orgId=1',  'Load Generator Performance Dashboard Link')
            stringParam( 'ProjectHomePage',  'https://perf-grafana.aspiresys.com/d/3Wfdid4Mz/projecthomepage?orgId=1',  'Project HomePage Dashboard Link')
            stringParam( 'TimeStamp',  '${BUILD_TIMESTAMP}',  'Build TimeStamp')
    		choiceParam( 'environmentName',  ['UAT', 'PERF'],  'Choose one')
        	choiceParam( 'ApplicationName',  ['AspireIntranet'],  'Select Application Name')
        	choiceParam( 'ScriptToRun',  ['JmeterIntranetScript', 'JmeterIntranetScript1'],  'Select Scripts to execute')
           	choiceParam( 'JenkinsHomePath',  ['C:/Program Files (x86)/Jenkins'],  'Enter Jenkins Home Path(Use Forward Slash)')
            choiceParam( 'ParentPath',  ['C:/APTF_2.0_SeleniumAPIJMeter'],  'Enter Parent Path')
        	choiceParam( 'JmeterPath',  ['C:/APTF_2.0_SeleniumAPIJMeter/apache-jmeter-5.1.1'],  'Select Jmeter Version to be execute')
        }


 
        pollSubjobs true
        steps {
            downstreamParameterized {
                trigger('APTF_2.0_Jmeter') {
                    parameters {
                        predefinedProps([JmeterBuildNumber: '${buildNumber}',JmeterNoOfThreads: '${NoOfThreads}',JmeterBuildNumber: '${buildNumber}',JmeterNoOfThreads:'${NoOfThreads}',
                                         JmeterRampUp:'${RampUp}',JmeterDuration:'${duration}',JmeterDuration:'${duration}',JmeterEnvironmentName:'${environmentName}',JmeterTestName:'${TestName}',
                                         JmeterApplicationName:'${ApplicationName}',JmeterScriptToRun:'${ScriptToRun}',FrameworkParentPath:'${ParentPath}',JmeterPath1:'${JmeterPath}',JenkinsHomePath1:'${JenkinsHomePath}',
                                         JmeterInfluxDBName:'${InfluxDBName}',JmeterInfluxDBIPAddress:'${InfluxDBIPAddress}',JmeterInfluxDBUrl:'${InfluxDBUrl}',JmeterScenarioLevelDashboardLink:'${ScenarioLevelDashboardLink}',
                                         JmeterBuildHistoryDashboardLink:'${BuildHistoryDashboardLink}',JmeterComparisonDashboardLink:'${ComparisonDashboardLink}',JmeterResposeCodeWiki:'${ResposeCodeWiki}',JmeterIndividualDashboard1:'${JmeterIndividualDashboard}',
                                         JmeterHomePageDashboard:'${HomePageDashboard}',JmeterStepLevelDashboard:'${StepLevelDashboard}',JmeterRequestLevelDashboard:'${RequestLevelDashboard}',LoadGeneratorPerformance1:'${LoadGeneratorPerformance}',
                                         ServerPerformance1:'${ServerPerformance}'])
                                }
                            }
                trigger('APTF_2.0_SeleniumAPI'){
                    parameters {
                        predefinedProps([SeleniumBuildNumber:'${buildNumber}',SeleniumRampUp:'${RampUp}',SeleniumDuration:'${duration}',SeleniumApplicationName:'${ApplicationName}',
                                         SeleniumEnvironmentName:'${environmentName}',SeleniumTestName:'${TestName}',SeleniumScriptToRun:'${SeleniumScriptName}',FrameworkParentPath:'${ParentPath}',
                                         JmeterPath1:'${JmeterPath}',JenkinsHomePath1:'${JenkinsHomePath}',SeleniumInfluxDBName:'${SeleniumDBName}',SeleniumWebDriverPath:'${WebDriverPath}',
                                         SeleniumExcelOutputPath:'${ExcelOutputPath}',SeleniumInfluxDBIPAddress:'${InfluxDBIPAddress}',SeleniumOverallDashboard1:'${SeleniumOverallDashboard}',
                                         SeleniumIndividualDashboard1:'${SeleniumIndividualDashboard}',RunhomePageDashboard1:'${RunhomePageDashboard}',ProjectHomePage1:'${ProjectHomePage}',
                                         SeleniumInfluxDBUrl:'${InfluxDBUrl}',StepLevelDashboard1:'${StepLevelDashboard}',RequestLevelDashboard1:'${RequestLevelDashboard}',ScenarioLevelDashboardLink1:'${ScenarioLevelDashboardLink}',
                                         JmeterInfluxDBName:'${InfluxDBName}',BuildTimeStamp:'${TimeStamp}'])
                               }                             
                            }
        }
    }

        publishers {
            groovyPostBuild('''import java.io.*;
                               import java.util.*;
//def sda= ${ApplicationName}
//get current build
def build = Thread.currentThread().executable;
def ApplicationName1 = build.buildVariableResolver.resolve("ApplicationName");
def ProjectHomePage1 = build.buildVariableResolver.resolve("ProjectHomePage");
def RunhomePageDashboard1 = build.buildVariableResolver.resolve("RunhomePageDashboard");
//def regexp= ".+?/job/([^/]+)/.*"
//def match = build  =~ regexp
//def jobName = match[0][1]
//def jobName =System.getenv('JOB_NAME')
def jobName  = manager.build.getProject().getName();
//${JOB_NAME}
// ${manager.build.getEnvironment(manager.listener)['BUILD_NUMBER'] }
// grafana url for aggregate dashboard - replace time stamp with %s
def buildNum = build.number;
//def buildNum1 = build.number-1;

def ProjectHomepage ="%s&from="
def RunHomePage="%s&var-RunID=%s-Run_%s"

// get build start and end time
def start1 = build.getStartTimeInMillis();
def end1 = start1 + build.getExecutor().getElapsedTime();

ProjectHomepage=String.format(ProjectHomepage, ProjectHomePage1)+"now%2Fy&to=now";
RunHomePage=String.format(RunHomePage, RunhomePageDashboard1, ApplicationName1, buildNum);

def link = "<a href='%s'>%s</a><br/>";
def sb = new StringBuilder();
sb.append(String.format(link, ProjectHomepage,"ProjectHomePage "))
sb.append(String.format(link, RunHomePage,"PerformanceTestMetrics"));

def labelMessage = build.buildVariableResolver.resolve("labelMessage");
// set build description
build.setDescription(sb.toString()+"\n"+labelMessage);''', Behavior.MarkFailed)
    
      
  }
}

}
def createjmeterjob(String maven_version)
{
    freeStyleJob('APTF_2.0_Jmeter_Downstream') {
  parameters {
      stringParam( 'JmeterMainJobName','','Value from Main Job')
      stringParam( 'JenkinsHomePath2','','Value from Main Job')
      stringParam( 'ParentPath2','','Value from Main Job')
      stringParam( 'MainJobbuildNumber','','Value from Main Job')
      stringParam( 'MainJobAppName','','Value from Main Job')
      stringParam( 'JmeterInfluxDBUrl1','','Jmeter InfluxDB Url')
      stringParam( 'JmeterBuildHistoryDashboardLink1','','Build History Dashboard Link')
      stringParam( 'JmeterComparisonDashboardLink1','','Comparison Dashboard Link')
      stringParam( 'JmeterResposeCodeWiki1','','Response Code Wiki Link')
      stringParam( 'JmeterHomePageDashboard1','','Home Page Dashboard Link')
      stringParam( 'JmeterInfluxDBName1','','Jmeter InfluxDB Name')
      }
  steps {
    maven {
        mavenInstallation('mavenhome')
        goals('install -DdatabaseName=${JmeterInfluxDBName1} -Ddburl=${JmeterInfluxDBUrl1}')
        rootPOM('${ParentPath2}/Sampleinflux_ErrorCount/pom.xml')


   }
   maven {
       mavenInstallation('mavenhome')
       goals('install -DdatabaseName=${JmeterInfluxDBName1} -Ddburl=${JmeterInfluxDBUrl1} -DOverallDashboardurl=${JmeterScenarioLevelDashboardLink1}')
       rootPOM('${ParentPath2}/OverallBuildsGrafana/pom.xml')


  }
  maven {
      mavenInstallation('mavenhome')
      goals('install -DdatabaseName=${JmeterInfluxDBName1} -Ddburl=${JmeterInfluxDBUrl1} -Dbuildhistory=${JmeterBuildHistoryDashboardLink1} -Dcomparisondashboard=${JmeterComparisonDashboardLink1} -Dresposecodewiki=${JmeterResposeCodeWiki1}')
      rootPOM('${ParentPath2}/InfluxCodeForMainDashboard/pom.xml')


 }

        }
 publishers {
        groovyPostBuild('''import java.io.*;
import java.util.*;
//def sda= ${ApplicationName}
//get current build
def build = Thread.currentThread().executable;
def jenkinspath1 = build.buildVariableResolver.resolve("JenkinsHomePath2");
def localPath = build.buildVariableResolver.resolve("ParentPath2");
def MainJobName1 = build.buildVariableResolver.resolve("JmeterMainJobName");
def MainJobbuildNumber1 = build.buildVariableResolver.resolve("MainJobbuildNumber");
def ApplicationName1 = build.buildVariableResolver.resolve("MainJobAppName");
def HomePageLink = build.buildVariableResolver.resolve("JmeterHomePageDashboard1");
//def regexp= ".+?/job/([^/]+)/.*"
//def match = build  =~ regexp
//def jobName = match[0][1]
//def jobName =System.getenv('JOB_NAME')
def jobName  = manager.build.getProject().getName();
//${JOB_NAME}
// ${manager.build.getEnvironment(manager.listener)['BUILD_NUMBER'] }
// grafana url for aggregate dashboard - replace time stamp with %s
def buildNum = build.number;
//def buildNum1 = build.number-1;

BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					"${jenkinspath1}/jobs/${MainJobName1}/builds/${MainJobbuildNumber1}/log"));
			String line = reader.readLine();
			int i=1;
			int j=1;
                         int p=1;
			while (line != null) {
				//System.out.println(line);
                                String Q2=line.matches("(.*)-JApplicationName=(.*?)");
				if(Q2.equals("true"))
				{
					if(p==1)
					{
						//System.out.println(line);
						String l=line;
				  		String[] out1 = l.split("JApplicationName=");
				  		String[] out2 = out1[1].split(" ");
				  		AppName=out2[0];
				  		//System.out.println(out2[0]);
				  		p++;
					}	
				}
				String Q=line.matches("Starting (.*) test @ (.*) (.*) (.*) (.*):(.*)(.*) (.*) (.*) ((.*?))");
				if(Q.equals("true"))
				{
					if(i==1)
					{
				  		String k=line;
				  		String[] out = k.split("\\(");
				  		start=out[1].substring(0, 13);
				  		//System.out.println(out[1].substring(0, 13));
				  		i++;
					}	
				}
   String lQ=line.matches("Starting (.*) test @ (.*) (.*) (.*) (.*):(.*)(.*) (.*) (.*) ((.*?))");
				if(lQ.equals("true"))
				{
					if(i==2)
					{
				  		String k=line;
				  		String[] out = k.split("\\(");
				  		Lightstart=out[1].substring(0, 13);
				  		//System.out.println(out[1].substring(0, 13));
				  		i++;
					}	
				}
				String Q1=line.matches("Tidying up (.*?) @ (.*) (.*) (.*) (.*):(.*):(.*) (.*) (.*) ((.*?))");
				if(Q1.equals("true"))
				{
					if(j==1)
					{
				  		String a=line;
				  		String[] put = a.split("\\(");
				  		end=put[1].substring(0, 13);
				  		//System.out.println(put[1].substring(0, 13));
				  		j++;
					}	
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

def BuildHomepage = "%s&from="

BuildHomepage = String.format(BuildHomepage, HomePageLink)+"now%2Fy&to=now";

// get build start and end time
def start1 = build.getStartTimeInMillis();
def end1 = start1 + build.getExecutor().getElapsedTime();

def link = "<a href='%s'>%s</a><br/>";
def sb = new StringBuilder();
sb.append(String.format(link,  BuildHomepage ,"HomePage"));

def labelMessage = build.buildVariableResolver.resolve("labelMessage");
// set build description
build.setDescription(sb.toString()+"\n"+labelMessage);''', Behavior.MarkFailed)
    }
}
}
def createjmeterjob(String jmeterdownstream,String maven_version)
{
    freeStyleJob('APTF_2.0_Jmeter') {
  parameters {
      stringParam( 'JmeterBuildNumber','','Jenkins BuildNo')
      stringParam( 'JmeterNoOfThreads','','Number Of Users')
      stringParam( 'JmeterRampUp','','RampUp duration (in seconds)')
      stringParam( 'JmeterDuration','','Duration (in seconds)')
      stringParam( 'JmeterTestName','','Enter Test Name')
      stringParam( 'JmeterScriptToRun','','Select Scripts to execute')
      stringParam( 'JenkinsHomePath1','','Enter Jenkins Home Path')
      stringParam( 'JobName','${JOB_NAME}','Jenkins Job Name')
      stringParam( 'JmeterInfluxDBName','','Enter DB Name')
      stringParam( 'JmeterInfluxDBIPAddress','','Enter InfluxDB Host IP Address')
      stringParam( 'JmeterEnvironmentName','','Environment Name')
      stringParam( 'JmeterApplicationName','','Application Name')
      stringParam( 'FrameworkParentPath','','Framework SetUp Path')
      stringParam( 'JmeterPath1','','Jmeter Home Path')
      stringParam( 'JmeterInfluxDBUrl','','Jmeter InfluxDB Url')
      stringParam( 'JmeterScenarioLevelDashboardLink','','Jmeter Scenario Level Dashboard Link')
      stringParam( 'JmeterBuildHistoryDashboardLink','','Build History Dashboard Link')
      stringParam( 'JmeterComparisonDashboardLink','','Comparison Dashboard Link')
      stringParam( 'JmeterResposeCodeWiki','','Response Code Wiki')
      stringParam( 'JmeterIndividualDashboard1','','Jmeter Individual Dashboard Link')
      stringParam( 'JmeterHomePageDashboard','','Jmeter Home Page Dashboard Link')
      stringParam( 'JmeterStepLevelDashboard','','Step Level Dashboard Link')
      stringParam( 'JmeterRequestLevelDashboard','','Request Level Dashboard Link')
      stringParam( 'LoadGeneratorPerformance1','','Load Generator Performance Dashboard Link')
      stringParam( 'ServerPerformance1','','Server Performance Dashboard Link')
      }
  steps {
            shell('cd $JmeterPath1/bin./jmeter -Jjmeter.save.saveservice.output_format=csv -Jjenkins.buildnumber=$JmeterApplicationName-Run_$JmeterBuildNumber  -n -t $FrameworkParentPath/Script/$JmeterScriptToRun.jmx -R 192.168.56.121 -JNoOfThreads=$JmeterNoOfThreads -JRampUp=$JmeterRampUp -Jduration=$JmeterDuration -JApplicationName=$JmeterApplicationName -JenvironmentName=$JmeterEnvironmentName -JTestName=$JmeterTestName -JParentPath=$FrameworkParentPath -JInfluxDBName=$JmeterInfluxDBName -JInfluxDBIPAddress=$JmeterInfluxDBIPAddress -l $FrameworkParentPath/Result/Jtl/Test_$JmeterBuildNumber.jtl -e -o $FrameworkParentPath/HTML_Results/Test_$JmeterBuildNumber')
            shell('cd $JmeterPath1/bin./jmeter -Jjmeter.save.saveservice.output_format=xml -Jjenkins.buildnumber=$JmeterApplicationName-Run_$JmeterBuildNumber -JParentPath=$FrameworkParentPath -n -t $FrameworkParentPath/DependencyScript/Lightning.jmx -R 192.168.56.121')
            shell('cd $JmeterPath1/bin./jmeter -Jjmeter.save.saveservice.output_format=xml -Jjenkins.buildnumber=Test_$JmeterBuildNumber -JParentPath=$FrameworkParentPath -JzipPath=$JenkinsHomePath1/workspace/$JobName -n -t $FrameworkParentPath/DependencyScript/ZipScript.jmx -R 192.168.56.121')
        }
 publishers {
        groovyPostBuild('''import java.io.*;import java.util.*;

//get current build
def build = Thread.currentThread().executable;
def applicationName1 = build.buildVariableResolver.resolve("JmeterApplicationName");
def jenkinspath = build.buildVariableResolver.resolve("JenkinsHomePath1");
def localPath = build.buildVariableResolver.resolve("FrameworkParentPath");
def JmeterStepLevelDashboard1= build.buildVariableResolver.resolve("JmeterStepLevelDashboard");
def JmeterIndividualDashboard2= build.buildVariableResolver.resolve("JmeterIndividualDashboard1");
def LoadGeneratorPerformance2= build.buildVariableResolver.resolve("LoadGeneratorPerformance1");
def JmeterRequestLevelDashboard1= build.buildVariableResolver.resolve("JmeterRequestLevelDashboard");
def JmeterScenarioLevelDashboardLink1= build.buildVariableResolver.resolve("JmeterScenarioLevelDashboardLink");
def ServerPerformance2= build.buildVariableResolver.resolve("ServerPerformance1");

//def regexp= ".+?/job/([^/]+)/.*"
//def match = build  =~ regexp
//def jobName = match[0][1]
//def jobName =System.getenv('JOB_NAME')
def jobName  = manager.build.getProject().getName();
//${JOB_NAME}
// ${manager.build.getEnvironment(manager.listener)['BUILD_NUMBER'] }
// grafana url for aggregate dashboard - replace time stamp with %s
def buildNum = build.number;
//def buildNum1 = build.number-1;

BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					"${jenkinspath}/jobs/${jobName}/builds/" +buildNum+ "/log"));
			String line = reader.readLine();
			int i=1;
			int j=1;
                         int p=1;
			while (line != null) {
				//System.out.println(line);
                                String Q2=line.matches("(.*)-JApplicationName=(.*?)");
				if(Q2.equals("true"))
				{
					if(p==1)
					{
						//System.out.println(line);
						String l=line;
				  		String[] out1 = l.split("JApplicationName=");
				  		String[] out2 = out1[1].split(" ");
				  		AppName=out2[0];
				  		//System.out.println(out2[0]);
				  		p++;
					}	
				}
				String Q=line.matches("Starting (.*) test @ (.*) (.*) (.*) (.*):(.*)(.*) (.*) (.*) ((.*?))");
				if(Q.equals("true"))
				{
					if(i==1)
					{
				  		String k=line;
				  		String[] out = k.split("\\(");
				  		start=out[1].substring(0, 13);
				  		//System.out.println(out[1].substring(0, 13));
				  		i++;
					}	
				}
   String lQ=line.matches("Starting (.*) test @ (.*) (.*) (.*) (.*):(.*)(.*) (.*) (.*) ((.*?))");
				if(lQ.equals("true"))
				{
					if(i==2)
					{
				  		String k=line;
				  		String[] out = k.split("\\(");
				  		Lightstart=out[1].substring(0, 13);
				  		//System.out.println(out[1].substring(0, 13));
				  		i++;
					}	
				}
				String Q1=line.matches("Tidying up (.*) @ (.*) (.*) (.*) (.*):(.*):(.*) (.*) (.*) ((.*?))");
				if(Q1.equals("true"))
				{
					if(j==1)
					{
				  		String a=line;
				  		String[] put = a.split("\\(");
				  		end=put[1].substring(0, 13);
				  		//System.out.println(put[1].substring(0, 13));
				  		j++;
					}	
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

def perfResult = "%s&from=%s&to=%s&var-buildno=%s-Run_%s"
def jmxMonitor = "%s&from=%s&to=%s&var-buildno=%s-Run_%s"
def telegraf ="%s&from=%s&to=%s&var-buildno=%s-Run_%s"
def Requestlevel = "%s&from=%s&to=%s&var-buildno=%s-Run_%s"
def telegraf1 ="%s&from=%s&to=%s&var-buildno=%s-Run_%s"
def telegrafServer ="%s&from=%s&to=%s&var-buildno=%s-Run_%s"



// get build start and end time
def start1 = build.getStartTimeInMillis();
def end1 = start1 + build.getExecutor().getElapsedTime();
// replace time
perfResult = String.format(perfResult, JmeterStepLevelDashboard1, start, end, AppName, buildNum);
jmxMonitor= String.format(jmxMonitor, JmeterIndividualDashboard2, start, end, AppName, buildNum);
telegraf=String.format(telegraf, LoadGeneratorPerformance2, start, end, AppName, buildNum);
telegraf1=String.format(telegraf1, JmeterScenarioLevelDashboardLink1, start, end, AppName, buildNum);
telegrafServer= String.format(telegrafServer, ServerPerformance2, start, end, AppName, buildNum);
Requestlevel =String.format(Requestlevel, JmeterRequestLevelDashboard1, start, end, AppName, buildNum);

def link = "<a href='%s'>%s</a><br/>";
def sb = new StringBuilder();
sb.append(String.format(link, telegraf1, "Scenario_Level _Metrics"))
.append(String.format(link, perfResult, "Step_Level_Metrics"))
.append(String.format(link, Requestlevel, "Request_Level_Metrics"))
.append(String.format(link, jmxMonitor, "Individual Step Metrics"))
.append(String.format(link, telegrafServer, "Server_Performance"))
.append(String.format(link, telegraf, "LoadGeneratorPerformance"))

def labelMessage = build.buildVariableResolver.resolve("labelMessage");
// set build description
build.setDescription(sb.toString()+"\n"+labelMessage);

String sourceFilePath ="${localPath}/lightning-example-master/bin/junit.xml"  
String destinationFilePath = "${jenkinspath}/workspace/${jobName}/junit.xml"  
def file = new File('${jenkinspath}/workspace/${jobName}/junit.xml')  
if(file.exists()) 
 file.delete()  
(new AntBuilder()).copy(file: sourceFilePath, tofile: destinationFilePath)


new AntBuilder().copy( file:"${localPath}/Result/BuildNo_${applicationName1}-Run_" +buildNum+ ".log", 
                           tofile:"${jenkinspath}/workspace/${jobName}/BuildNo_${applicationName1}-Run_" +buildNum+ ".log")
new AntBuilder().copy( file:"${localPath}/Result/BuildNo_${applicationName1}-Run_" +buildNum+ ".xml", 
                           tofile:"${jenkinspath}/workspace/${jobName}/BuildNo_${applicationName1}-Run_" +buildNum+ ".xml")
new AntBuilder().copy( file:"${localPath}/Result/BuildNo_${applicationName1}-Run_" +buildNum+ ".jtl", 
                           tofile:"${jenkinspath}/workspace/${jobName}/BuildNo_${applicationName1}-Run_" +buildNum+ ".jtl")''', Behavior.MarkFailed)
    }
      publishers {
        archiveJunit('junit.xml') {
            allowEmptyResults()
            retainLongStdout()
            healthScaleFactor(1.0)
            
        }
      }
  publishers {
        postBuildScripts {
            steps {
                shell('cd $JmeterPath1/bin./jmeter -Jjmeter.save.saveservice.output_format=xml -Jjenkins.buildnumber=$JmeterBuildNumber -JjobName=$JobName -JjenkinsHomePath=$JenkinsHomePath1 -JoverallDashboardLink=$JmeterScenarioLevelDashboardLink -n -t $FrameworkParentPath/DependencyScript/SMTP_Stable.jmx')
            }  
              onlyIfBuildSucceeds(true)
        }
    postBuildScripts {
            steps {
                shell('cd $JmeterPath1/bin./jmeter -Jjmeter.save.saveservice.output_format=xml -Jjenkins.buildnumber=$JmeterBuildNumber -JjobName=$JobName -JjenkinsHomePath=$JenkinsHomePath1 -JoverallDashboardLink=$JmeterScenarioLevelDashboardLink -n -t $FrameworkParentPath/DependencyScript/SMTP_Unstable.jmx')
            } 
      		onlyIfBuildSucceeds(false)
             markBuildUnstable(true)
    }
    
    
    postBuildScripts {
            steps {
                shell(' cd $JmeterPath1/bin./jmeter -Jjmeter.save.saveservice.output_format=xml -Jjenkins.buildnumber=$JmeterBuildNumber -JjobName=$JobName -JjenkinsHomePath=$JenkinsHomePath1 -JoverallDashboardLink=$JmeterScenarioLevelDashboardLink -n -t $FrameworkParentPath/DependencyScript/SMTP_Failure.jmx')
            }  
              onlyIfBuildSucceeds(false)
              onlyIfBuildFails(true)
    } 
        }
    publishers {
        downstreamParameterized {
            trigger('APTF_2.0_Jmeter_Downstream') {
              markBuildStable = true
                parameters {
                    predefinedProps([JmeterMainJobName:'${JobName}',JenkinsHomePath2:'${JenkinsHomePath1}',ParentPath2:'${FrameworkParentPath}',
                                     MainJobbuildNumber:'${JmeterBuildNumber}',
                                     MainJobAppName:'${JmeterApplicationName}',
                                     JmeterInfluxDBUrl1:'${JmeterInfluxDBUrl}',
                                     JmeterScenarioLevelDashboardLink1:'${JmeterScenarioLevelDashboardLink}',
                                     JmeterBuildHistoryDashboardLink1:'${JmeterBuildHistoryDashboardLink}',
                                     JmeterComparisonDashboardLink1:'${JmeterComparisonDashboardLink}',
                                     JmeterResposeCodeWiki1:'${JmeterResposeCodeWiki}',
                                     JmeterHomePageDashboard1:'${JmeterHomePageDashboard}',
                                     JmeterInfluxDBName1:'${JmeterInfluxDBName}'])
                }
                }
                
            }
            }
  
  
    }
}
def createjmeterjob(String seleniumjobname,String maven_version)
{
    freeStyleJob('APTF_2.0_SeleniumAPI') {
        parameters {
            stringParam( 'SeleniumBuildNumber','','Jenkins BuildNo')
            stringParam( 'SeleniumRampUp','','RampUp duration (in seconds)')
            stringParam( 'SeleniumDuration','','Duration (in seconds)')
            stringParam( 'SeleniumScriptToRun','','Select Scripts to execute')
            stringParam( 'JenkinsHomePath1','','Enter Jenkins Home Path')
            stringParam( 'JobName','${JOB_NAME}','Jenkins Job Name')
            stringParam( 'SeleniumInfluxDBName','','Enter DB Name')
            stringParam( 'SeleniumInfluxDBIPAddress','','Enter InfluxDB Host IP Address')
            stringParam( 'SeleniumWebDriverPath','','Web Driver Path')
            stringParam( 'SeleniumExcelOutputPath','','Excel Output Path')
            stringParam( 'SeleniumApplicationName','','Application Name')
            stringParam( 'FrameworkParentPath','','Framework Parent Path')
            stringParam( 'JmeterPath1','','Jmeter Home Path')
            stringParam( 'SeleniumOverallDashboard1','','Selenium Overall Dashboard')
            stringParam( 'SeleniumIndividualDashboard1','','Selenium Individual Dashboard')
            stringParam( 'RunhomePageDashboard1','','Run Home Page Dashboard')
            stringParam( 'ProjectHomePage1','','Project Home Page Dashboard')
            stringParam( 'SeleniumInfluxDBUrl','','Selenium Influx DB Url')
            stringParam( 'StepLevelDashboard1','','Step Level Dashboard')
            stringParam( 'ScenarioLevelDashboardLink1','','Scenario Level Dashboard')
            stringParam( 'JmeterInfluxDBName','','Jmeter InfluxDB Name')
            stringParam( 'BuildTimeStamp','','Build TimeStamp')
            stringParam( 'RequestLevelDashboard1','','Request Level Dashboard')
            stringParam( 'SeleniumEnvironmentName','','SeleniumEnvironmentName')
            stringParam( 'SeleniumTestName','','SeleniumTestName')
      }
        steps {
            shell('cd $JmeterPath1/bin./jmeter -Jjmeter.save.saveservice.output_format=csv -Gselenium.buildnumber=$SeleniumApplicationName-Run_$SeleniumBuildNumber  -n -t $FrameworkParentPath/Script/$SeleniumScriptToRun.jmx -R 192.168.56.120 -JBackendBuildnumber=$SeleniumApplicationName-Run_$SeleniumBuildNumber -JBackendInfluxDBName=$SeleniumInfluxDBName -GInfluxDBUrl=$SeleniumInfluxDBUrl -JInfluxDBIP=$SeleniumInfluxDBIPAddress -GWorkBookName=Perfresult$BuildTimeStamp -JApplicationName=$SeleniumApplicationName -JenvironmentName=$SeleniumEnvironmentName -JTestName=$SeleniumTestName -GRampUp=$SeleniumRampUp -Gduration=$SeleniumDuration -GSeleniumInfluxDBName=$SeleniumInfluxDBName -GWebdriverPath=$SeleniumWebDriverPath -GExcelPath=$SeleniumExcelOutputPath -l $FrameworkParentPath/Result/Jtl/SeleniumTest_$SeleniumBuildNumber.jtl')
            shell('cd $JmeterPath1/bin./jmeter -n -t $FrameworkParentPath/Script/FilterDataToDashboard.jmx -R 192.168.56.120 -JWorkBookName=Perfresult$BuildTimeStamp -GInfluxDBUrl=$SeleniumInfluxDBUrl -Gselenium.buildnumber=$SeleniumApplicationName-Run_$SeleniumBuildNumber -GSeleniumInfluxDBName=$SeleniumInfluxDBName -GWebdriverPath=$SeleniumWebDriverPath -GExcelPath=$SeleniumExcelOutputPath')
           
    
        maven {
                  mavenInstallation('mavenhome')
                  goals('install -DDBName=${JmeterInfluxDBName} -DDBUrl=${SeleniumInfluxDBUrl} -DSeleniumDB=${SeleniumInfluxDBName} -DOverallDashboardLink=${ScenarioLevelDashboardLink1} -DRequestLevelDashboard=${RequestLevelDashboard1} -DStepLevelDashboard=${StepLevelDashboard1} -DSeleniumOverall=${SeleniumOverallDashboard1} -DSeleniumIndividual=${SeleniumIndividualDashboard1} -DSeleniumRunhomePage=${RunhomePageDashboard1}')
        		  rootPOM('${FrameworkParentPath}/MavenCodeForAPIPerformance/pom.xml')

        
             }
    
    
    }
    
    
    
  
        publishers {
            groovyPostBuild('''import java.io.*;
                               import java.util.*;

//get current build
def build = Thread.currentThread().executable;
def jenkinspath = build.buildVariableResolver.resolve("JenkinsHomePath1");
def localPath = build.buildVariableResolver.resolve("FrameworkParentPath");
def applicationName1 = build.buildVariableResolver.resolve("SeleniumApplicationName");
def SeleniumIndividual = build.buildVariableResolver.resolve("SeleniumIndividualDashboard1");
def SeleniumOverall = build.buildVariableResolver.resolve("SeleniumOverallDashboard1");
//def regexp= ".+?/job/([^/]+)/.*"
//def match = build  =~ regexp
//def jobName = match[0][1]
//def jobName =System.getenv('JOB_NAME')
def jobName  = manager.build.getProject().getName();
//${JOB_NAME}
// ${manager.build.getEnvironment(manager.listener)['BUILD_NUMBER'] }
// grafana url for aggregate dashboard - replace time stamp with %s
def buildNum = build.number;
//def buildNum1 = build.number-1;

BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					"${jenkinspath}/jobs/${jobName}/builds/" +buildNum+ "/log"));
			String line = reader.readLine();
			int i=1;
			int j=1;
                         int p=1;
			while (line != null) {
				String Q=line.matches("Starting (.*) test @ (.*) (.*) (.*) (.*):(.*)(.*) (.*) (.*) ((.*?))");
				if(Q.equals("true"))
				{
					if(i==1)
					{
				  		String k=line;
				  		String[] out = k.split("\\(");
				  		start=out[1].substring(0, 13);
				  		//System.out.println(out[1].substring(0, 13));
				  		i++;
					}
				}
				String Q1=line.matches("Tidying up (.*) @ (.*) (.*) (.*) (.*):(.*):(.*) (.*) (.*) ((.*?))");
				if(Q1.equals("true"))
				{
					if(j==1)
					{
				  		String a=line;
				  		String[] put = a.split("\\(");
				  		end=put[1].substring(0, 13);
				  		//System.out.println(put[1].substring(0, 13));
				  		j++;
					}
				}
                               String Q2=line.matches("Selenium End Time Before Filter Excel Data ((.*))");
                               if(Q2.equals("true"))
				{
				  		String a=line;
				  		String[] put = a.split("\\(");
				  		end=put[1].substring(0, 13);
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

def seleniumIndividual="%s&from=%s&to=%s&var-buildno=%s-Run_%s"
def seleniumOverall="%s&from=%s&to=%s&var-buildno=%s-Run_%s"


// get build start and end time
def start1 = build.getStartTimeInMillis();
def end1 = start1 + build.getExecutor().getElapsedTime();
// replace time
seleniumIndividual=String.format(seleniumIndividual, SeleniumIndividual, start, end, applicationName1, buildNum);
seleniumOverall=String.format(seleniumOverall, SeleniumOverall, start, end, applicationName1, buildNum);

def link = "<a href='%s'>%s</a><br/>";
def sb = new StringBuilder();
sb.append(String.format(link, seleniumIndividual, "Selenium_Individual_Metrics"))
.append(String.format(link, seleniumOverall, "Selenium_Overall_Metrics"))

def labelMessage = build.buildVariableResolver.resolve("labelMessage");
// set build description
build.setDescription(sb.toString()+"\n"+labelMessage);''')
 }
 }
}

