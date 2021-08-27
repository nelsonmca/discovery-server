package io.nelson.nadar.discoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

import com.netflix.appinfo.AmazonInfo;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryServerApplication.class, args);
	}

	@Bean
	@Profile("!default")
	public EurekaInstanceConfigBean eurekaInstanceConfigBean(InetUtils utils) {
		final int managementPort = 8761;

		//log.info("Setting AmazonInfo on EurekaInstanceConfigBean");
		final EurekaInstanceConfigBean instance = new EurekaInstanceConfigBean(utils) {

			// needed only when Eureka server instance binds to EIP
			@Scheduled(initialDelay = 10000L, fixedRate = 30000L)
			public void refreshInfo() {
				//log.debug("Checking datacenter info changes");
				AmazonInfo newInfo = AmazonInfo.Builder.newBuilder().autoBuild("eureka");
				if (!this.getDataCenterInfo().equals(newInfo)) {
					//log.info("Updating datacenterInfo");
					((AmazonInfo) this.getDataCenterInfo()).setMetadata(newInfo.getMetadata());
				}
			}

			private AmazonInfo getAmazonInfo() {
				return (AmazonInfo) getDataCenterInfo();
			}

			@Override
			public String getHostname() {
				AmazonInfo info = getAmazonInfo();
				final String publicHostname = info.get(AmazonInfo.MetaDataKey.publicHostname);
				return this.isPreferIpAddress() ? info.get(AmazonInfo.MetaDataKey.localIpv4)
						: publicHostname == null ? info.get(AmazonInfo.MetaDataKey.localHostname) : publicHostname;
			}

			@Override
			public String getHostName(final boolean refresh) {
				return getHostname();
			}

			@Override
			public int getNonSecurePort() {
				return managementPort;
			}

			@Override
			public String getHomePageUrl() {
				return super.getHomePageUrl();
			}

			@Override
			public String getStatusPageUrl() {
				String scheme = getSecurePortEnabled() ? "https" : "http";
				return scheme + "://" + getHostname() + ":" + managementPort + getStatusPageUrlPath();
			}

			@Override
			public String getHealthCheckUrl() {
				String scheme = getSecurePortEnabled() ? "https" : "http";
				return scheme + "://" + getHostname() + ":" + managementPort + getHealthCheckUrlPath();
			}
		};

		AmazonInfo info = AmazonInfo.Builder.newBuilder().autoBuild("cloudconfig");
		instance.setDataCenterInfo(info);

		return instance;
	}

}
