import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../service/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username = ''
  password = ''
  errorMsg = '';
  invalidLogin = false

  constructor(private authentocationService: AuthenticationService,private router: Router,
    private loginservice: AuthenticationService) { }

  ngOnInit() {
  }

  checkLogin() {
    if (this.loginservice.authenticate(this.username, this.password)
    ) {
      this.router.navigate([''])
      this.invalidLogin = false
       this.updateRefreshTime();
    } else{
      this.errorMsg = "Invalid User Name and Password";
      this.invalidLogin = true
    }
  }

    private updateRefreshTime() {
    setTimeout(()=>{ 
     this.authentocationService.logOut();
    this.router.navigate(['login']);
 }, 300000);
  }

}