SELECT ip_address, COUNT(ip_address) as total_requests FROM logparser WHERE start_date BETWEEN "2017-01-01.13:00:00" AND "2017-01-01.14:00:00" GROUP BY (ip_address) HAVING total_requests > 100;