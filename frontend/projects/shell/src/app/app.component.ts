import { Component, inject, signal } from '@angular/core'
import { RouterOutlet } from '@angular/router'
import { ConfigService } from '@ycyw/shared'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  imports: [RouterOutlet],
})
export class App {
  protected readonly title = signal<string>('')

  private readonly configService = inject(ConfigService)

  constructor() {
    const appConfig = this.configService.config

    this.title.set(appConfig.appName)
  }
}
