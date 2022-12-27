--Created By Jason Hammar on 9/26/2022
--ID: 012568517
Connect to sample;
select ID, name, years from staff where years >= 10 order by years desc;
select ID, name, (salary + comm) as TOTAL_COMPENSATION from staff where comm is not null;
select ID, name, salary from staff where comm is null order by salary asc limit 5;
select deptname, location, name from staff join org on dept = deptnumb;
select job, count(job) as EMPLOYEE_COUNT from staff group by job order by count(job) desc;
select division, count(dept) as EMPLOYEE_COUNT from org join staff on deptnumb = dept group by division order by count(dept) asc;
