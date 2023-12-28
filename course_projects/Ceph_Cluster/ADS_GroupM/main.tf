provider "google"{
    project     = "ceph-407416"
    credentials = "/home/utilizador/4ano/adm_sys/project/learn-terraform-docker-container/ceph-407416-cfa382d7b870.json"
    region      = "europe-west4"
    zone        = "europe-west4-a"
}

//vpc-cluser is just an  identifier to my-vpc-cluster
resource "google_compute_network" "vpc-cluster" {
    name                    = "my-vpc-cluster"
    auto_create_subnetworks = "true"
}

resource "google_compute_subnetwork" "vpc-subnet" {
    name          = "my-vpc-subnet"
    ip_cidr_range = "10.0.1.0/24"
    region        = "europe-west4"
    network       = google_compute_network.vpc-cluster.id
  
}

//firewall config
resource "google_compute_firewall" "ceph-internal"{
    name = "allow-ceph-internal"
    network = google_compute_network.vpc-cluster.name

    allow {
      protocol = "tcp"
      ports    = ["3300", "6789", "6800-7300"] // Portas padrão do Ceph
    }

    source_ranges = ["10.0.1.0/24"]
}

resource "google_compute_firewall" "allow-ssh" {
  name    = "allow-ssh"
  network = google_compute_network.vpc-cluster.name

  allow {
    protocol = "tcp"
    ports    = ["22"]
  }

  source_ranges = ["0.0.0.0/0"]
}

resource "google_compute_firewall" "allow-icmp" {
  name    = "allow-icmp"
  network = google_compute_network.vpc-cluster.name

  allow {
    protocol = "icmp"
  }

  source_ranges = ["10.0.1.0/24"]
}

//creating a disk to osd2


//creates extern static ip's address
resource "google_compute_address" "static_ip_rcli" {
  name   = "rcli-static-ip"
  region = "europe-west4"
}
resource "google_compute_address" "static_ip_osd1" {
  name   = "osd1-static-ip"
  region = "europe-west4"
}

resource "google_compute_address" "static_ip_osd2" {
  name   = "osd2-static-ip"
  region = "europe-west4"
}

resource "google_compute_address" "static_ip_mon" {
  name   = "mon-static-ip"
  region = "europe-west4"
}

resource "google_compute_address" "static_ip_mgr" {
  name   = "mgr-static-ip"
  region = "europe-west4"
}

resource "google_compute_instance" "rcli" {
  name         = "rcli"
  machine_type = "e2-micro"
  zone         = "europe-west4-a"
  allow_stopping_for_update = true

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20231101"
      size = 10
    }
  }

  metadata = {
    ssh-keys = "ubuntu:${file("~/.ssh/id_ed25519.pub")}"
  }

  network_interface {
    network = google_compute_network.vpc-cluster.name
    subnetwork = google_compute_subnetwork.vpc-subnet.name
    access_config {
      nat_ip = google_compute_address.static_ip_rcli.address
    }

    //define static intern ip
    network_ip = "10.0.1.14"
  }
}

resource "google_compute_instance" "osd1" {
  name         = "osd1"
  machine_type = "e2-micro"
  zone         = "europe-west4-a"
  allow_stopping_for_update = true

  metadata = {
    ssh-keys = "ubuntu:${file("~/.ssh/id_ed25519.pub")}"
  }

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20231101"
      size = 10
    }
  }

  network_interface {
    network = google_compute_network.vpc-cluster.name
    subnetwork = google_compute_subnetwork.vpc-subnet.name
    access_config {
      nat_ip = google_compute_address.static_ip_osd1.address
    }

    //define static intern ip
    network_ip = "10.0.1.10"
  }
  lifecycle {
    ignore_changes = [attached_disk]
  }
}

resource "google_compute_disk" "osd1_disk" {
  name  = "osd1-disk"
  type  = "pd-standard"
  zone  = "europe-west4-a"
  size  = 10
  image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20231101"
}

resource "google_compute_attached_disk" "osd1_attached_disk" {
  disk     = google_compute_disk.osd1_disk.id
  instance = google_compute_instance.osd1.id
}


resource "google_compute_instance" "osd2" {
  name         = "osd2"
  machine_type = "e2-micro"
  zone         = "europe-west4-a"
  allow_stopping_for_update = true

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20231101"
      size = 10
    }
  }

  metadata = {
    ssh-keys = "ubuntu:${file("~/.ssh/id_ed25519.pub")}"
  }

  network_interface {
    network = google_compute_network.vpc-cluster.name
    subnetwork = google_compute_subnetwork.vpc-subnet.name
    access_config {
      nat_ip = google_compute_address.static_ip_osd2.address
    }

    //define static intern ip
    network_ip = "10.0.1.11"
  }
  lifecycle {
    ignore_changes = [attached_disk]
  }
}

resource "google_compute_disk" "osd2_disk" {
  name  = "osd2-disk"
  type  = "pd-standard"
  zone  = "europe-west4-a"
  size  = 10
  image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20231101"
}
              
resource "google_compute_attached_disk" "osd2_attached_disk" {
  disk     = google_compute_disk.osd2_disk.id
  instance = google_compute_instance.osd2.id
}


resource "google_compute_instance" "mon" {
  name         = "mon"
  machine_type = "e2-micro"
  zone         = "europe-west4-a"
  allow_stopping_for_update = true

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20231101"
      size = 10
    }
  }

  metadata = {
    ssh-keys = "ubuntu:${file("~/.ssh/id_ed25519.pub")}"
  }

  network_interface {
    network = google_compute_network.vpc-cluster.name
    subnetwork = google_compute_subnetwork.vpc-subnet.name
    access_config {
      nat_ip = google_compute_address.static_ip_mon.address
    }

    //define static intern ip
    network_ip = "10.0.1.12"
  }
}
resource "google_compute_instance" "mgr" {
  name         = "mgr"
  machine_type = "e2-micro"
  zone         = "europe-west4-a"
  allow_stopping_for_update = true

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20231101"
      size = 10
    }
  }

  metadata = {
    ssh-keys = "ubuntu:${file("~/.ssh/id_ed25519.pub")}"
  }

  network_interface {
    network = google_compute_network.vpc-cluster.name
    subnetwork = google_compute_subnetwork.vpc-subnet.name
    access_config {
      nat_ip = google_compute_address.static_ip_mgr.address
    }

    //define static intern ip
    network_ip = "10.0.1.13"
  }
}
