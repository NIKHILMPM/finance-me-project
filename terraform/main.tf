provider "aws" {
  region = "us-east-1"
}

resource "aws_instance" "finance_ec2" {
  ami           = "ami-0f9de6e2d2f067fca"
  instance_type = "t2.micro"
  associate_public_ip_address = true

  tags = {
    Name = "FinanceMe-Server"
  }

  vpc_security_group_ids = [aws_security_group.finance_sg.id]
}

resource "aws_security_group" "finance_sg" {
  name        = "finance_sg"
  description = "Allow SSH and 8081"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 8081
    to_port     = 8081
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

output "ec2_public_ip" {
  value = aws_instance.finance_ec2.public_ip
}
