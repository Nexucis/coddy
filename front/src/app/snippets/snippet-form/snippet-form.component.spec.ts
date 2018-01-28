import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SnippetFormComponent } from './snippet-form.component';
import { SharedModule } from '../../shared/shared.module';
import { HttpModule } from '@angular/http';
import { RouterTestingModule } from '@angular/router/testing';
import { SnippetService } from '../shared/snippet.service';
import { AuthModule } from '../../auth/auth.module';
import { LanguageService } from '../shared/language.service';
import { ToastService } from '../../core/template/toast.service';

describe('SnippetFormComponent', () => {
  let component: SnippetFormComponent;
  let fixture: ComponentFixture<SnippetFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SnippetFormComponent ],
      imports: [ SharedModule, AuthModule, HttpModule, RouterTestingModule.withRoutes([]) ],
      providers: [ SnippetService, LanguageService, ToastService ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SnippetFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});