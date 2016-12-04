# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|
  # CoreOS ist eine minimale Linux-Version für Docker-Container
  config.vm.box = "coreos-stable" # Image ist 1/2 der Größe von Ubuntu und hat schon Docker installiert
  config.vm.box_url = "http://stable.release.core-os.net/amd64-usr/current/coreos_production_vagrant.json"
  
  config.vm.provider "virtualbox" do |v|
    v.memory = 512
  end
  
  # Start Consul - url is http://192.168.0.5:8500/ui/
  config.vm.define "consul" do |conf|
    conf.vm.hostname = "consul"
	conf.vm.network "private_network", ip: "192.168.0.5"

	conf.vm.provision "shell", run: "always", inline: <<-SHELL
	
		# Wir nutzen Docker, um Consul zu installieren & zu starten. Zu mehr nicht.
		docker run -d --net=host \
				   voxxit/consul agent -server -bootstrap-expect 3 \
		           -data-dir /data -ui-dir /ui -client=192.168.0.5 \
				   -bind=192.168.0.5
		
    SHELL
  end
  config.vm.define "consul2" do |conf|
    conf.vm.hostname = "consul2"
	conf.vm.network "private_network", ip: "192.168.0.6"

	conf.vm.provision "shell", run: "always", inline: <<-SHELL
	
		# Wir nutzen Docker, um Consul zu installieren & zu starten. Zu mehr nicht.
		docker run -d --net=host \
				   voxxit/consul agent -server -bootstrap-expect 3 \
		           -data-dir /data -join=192.168.0.5 -client=192.168.0.6 \
				   -bind=192.168.0.6
		
    SHELL
  end
  config.vm.define "consul3" do |conf|
    conf.vm.hostname = "consul3"
	conf.vm.network "private_network", ip: "192.168.0.7"

	conf.vm.provision "shell", run: "always", inline: <<-SHELL
	
		# Wir nutzen Docker, um Consul zu installieren & zu starten. Zu mehr nicht.
		docker run -d --net=host \
				   voxxit/consul agent -server -bootstrap-expect 3 \
		           -data-dir /data -join=192.168.0.5 -client=192.168.0.7 \
				   -bind=192.168.0.7
		
    SHELL
  end
end
