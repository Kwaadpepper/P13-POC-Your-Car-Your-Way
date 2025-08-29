import { NgOptimizedImage } from '@angular/common'
import { Component, OnDestroy, inject } from '@angular/core'
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms'
import { Router } from '@angular/router'

import { ButtonModule } from 'primeng/button'
import { InputTextModule } from 'primeng/inputtext'
import { MessageModule } from 'primeng/message'
import { Subject, takeUntil } from 'rxjs'

import { BackButton } from '@ycyw/shell-shared/components'
import { ToastService } from '@ycyw/shell-shared/services'

import { LoginViewModel } from './login-viewmodel'

@Component({
  selector: 'shell-login',
  imports: [
    ButtonModule, BackButton,
    InputTextModule, ReactiveFormsModule,
    MessageModule,
    NgOptimizedImage,
  ],
  providers: [LoginViewModel],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnDestroy {
  public form = new FormGroup({
    login: new FormControl('', {
      validators: [
        Validators.required,
      ],
      nonNullable: true,
    }),
    password: new FormControl('', {
      validators: [
        Validators.required,
      ],
      nonNullable: true,
    }),
  })

  private readonly endObservables = new Subject<true>()

  public readonly toastService = inject(ToastService)

  public readonly viewModel = inject(LoginViewModel)

  public readonly router = inject(Router)

  constructor() {
    this.form.valueChanges.subscribe((value) => {
      this.viewModel.login.set(value.login ?? '')
      this.viewModel.password.set(value.password ?? '')
    })
  }

  ngOnDestroy(): void {
    this.endObservables.next(true)
    this.endObservables.complete()
  }

  public onSubmit(): void {
    const { login, password } = this.viewModel
    if (this.form.invalid) {
      this.toastService.error('Tous les champs sont obligatoires')
      return
    }

    this.viewModel
      .logginAndSetSession(login(), password())
      .pipe(takeUntil(this.endObservables))
      .subscribe({
        next: () => {
          this.router.navigateByUrl('/')
        },
      })
  }

  public demoLogin(login: string, password: string): void {
    this.viewModel.login.set(login)
    this.viewModel.password.set(password)
    this.form.setValue({ login, password })
    this.onSubmit()
  }
}
