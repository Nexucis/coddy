import { AdminGuard } from './guard/admin.guard';
import { PrivateGuard } from './guard/private.guard';
import { NavbarComponent } from './template/navbar/navbar.component';
import { TemplateComponent } from './template/template.component';
import { AuthModule } from '../auth/auth.module';
import { SharedModule } from '../shared/shared.module';
import { CoreRoutingModule } from './core-routing.module';
import { HomeComponent } from './home/home.component';
import { OverviewComponent } from './overview/overview.component';
import { NgModule } from '@angular/core';

@NgModule({
  imports: [
    SharedModule,
    CoreRoutingModule
  ],
  exports: [
    TemplateComponent
  ],
  declarations: [
    TemplateComponent,
    NavbarComponent,
    OverviewComponent,
    HomeComponent    
  ],
  providers: [
    PrivateGuard,
    AdminGuard
  ]
})
export class CoreModule { }