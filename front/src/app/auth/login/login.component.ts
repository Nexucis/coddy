import { MessageType } from '../../shared/message/message-type';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { TokenService } from '../token.service';
import { LoginService } from './login.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  MESSAGE_TYPE: typeof MessageType = MessageType;

  login = '';
  password = '';
  isLoading = false;
  redirect = false;
  tokenHasExpired = false;
  error: Error;

  redirectTo = '/overview';

  constructor(
    private loginService: LoginService,
    private tokenService: TokenService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.redirect = !!params.get('redirect');
      this.tokenHasExpired = !!params.get('tokenHasExpired');
    });
    this.route.params.subscribe((params: { redirect: boolean, tokenHasExpired: boolean }) => {
      this.redirect = params.redirect;
      this.tokenHasExpired = params.tokenHasExpired;
    });
  }

  authenticate() {
    this.isLoading = true;
    this.error = null;

    this.loginService.authenticate(this.login, this.password).finally(
      () => this.isLoading = false
    ).subscribe(
      token => {
        this.tokenService.setToken(token);
        this.router.navigate([this.redirectTo]);
      },
      err => this.error = err
      );
  }
}
