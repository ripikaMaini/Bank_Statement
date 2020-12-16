import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

export class Employee{
  constructor(
    public accountId:string,
    public fromAmount:string,
    public toAmount:string,
    public fromDate:string,
    public toDate:string,
  ) {}
}

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {
  constructor(private httpClient:HttpClient) {  }     
  generateStatement(emp:Employee){   
     return this.httpClient.post('http://localhost:8080/employees', emp);
  }
  login(userName,Password){
    return this.httpClient.get('http://localhost:8080/employees/login?'+ 'userName='+userName+'&password='+Password);
  }
}