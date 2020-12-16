import { Component, OnInit } from '@angular/core';
import { HttpClientService, Employee } from '../service/httpclient.service';
import { FormBuilder, FormGroup,FormControl } from '@angular/forms';

@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css']
})
export class EmployeeComponent implements OnInit {
  employees:Employee[]; 
  isStatement:boolean=false; 
  errorMsg:any; 
   
  constructor(private httpClientService:HttpClientService,private fb: FormBuilder) { }

 ngOnInit() {   
   this.errorMsg="";    
  }
   myForm = this.fb.group({
    accountId: '',
    fromAmount: '',
    toAmount: '',
    fromDate:'',
    toDate:''
  });

generate(){
  this.isStatement=false;
  if(this.validations()){  
   this.httpClientService.generateStatement(this.myForm.value).subscribe(
      response =>this.handleSuccessfulResponse(response),
     );
}}


validations(): boolean {
  var user = sessionStorage.getItem("username");
      if(user=="testUser"&&((this.myForm.value.fromAmount!=null&&this.myForm.value.fromAmount!="")
       ||(this.myForm.value.toAmount!=null&&this.myForm.value.toAmount!="")
       ||(this.myForm.value.fromDate!=null&&this.myForm.value.fromDate!="")
       ||(this.myForm.value.toDate!=null&&this.myForm.value.toDate!="")
       ||(this.myForm.value.accountId!=null&&this.myForm.value.accountId!=""))
      ){
       this.errorMsg="You are not authorised to generate the statement with any parameter";
          return false;
      }else if((this.myForm.value.fromAmount==null||this.myForm.value.fromAmount=="")
       && (this.myForm.value.toAmount!=null&&this.myForm.value.toAmount!="")){
         this.errorMsg="From Amount is required";
          return false;
       }else if((this.myForm.value.toAmount==null||this.myForm.value.toAmount=="")
        && (this.myForm.value.fromAmount!=null&&this.myForm.value.fromAmount!="")){
         this.errorMsg="To Amount is required";
          return false;
       }else if((this.myForm.value.fromDate==null|| this.myForm.value.fromDate=="") &&
       ( this.myForm.value.toDate!=null&&this.myForm.value.toDate!="")){
         this.errorMsg="From Date is required";
          return false;
       }else if((this.myForm.value.toDate==null||this.myForm.value.toDate=="")
        && (this.myForm.value.fromDate!=null&&this.myForm.value.fromDate!="")){
         this.errorMsg="To Date is required";
          return false;
       }return true;
    }

handleSuccessfulResponse(response)
{
  if(response == null){
   this.isStatement=false;
   this.errorMsg='Unable to fetch record!';
   }else{
  if(response.length>0){
    this.employees=response;
    this.isStatement=true;
    this.errorMsg='';    
   }else{
    this.isStatement=false;
    this.errorMsg='No data found!';
  }
}
    
}

}