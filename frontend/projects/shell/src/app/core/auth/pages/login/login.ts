import { Component, OnDestroy, inject } from '@angular/core'
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms'
import { Router } from '@angular/router'
import { redirectUrls } from '@shell-core/auth/routes'
import { BackButton } from '@shell-shared/components'
import { ToastService } from '@shell-shared/services'
import { ButtonModule } from 'primeng/button'
import { InputTextModule } from 'primeng/inputtext'
import { MessageModule } from 'primeng/message'
import { Subject, takeUntil } from 'rxjs'

import { LoginViewModel } from './login-viewmodel'

@Component({
  selector: 'shell-login',
  imports: [
    ButtonModule, BackButton,
    InputTextModule, ReactiveFormsModule,
    MessageModule,
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
          this.router.navigateByUrl(redirectUrls.authHomeUrl)
        },
      })
  }
}
