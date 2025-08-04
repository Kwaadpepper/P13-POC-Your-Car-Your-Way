import { loadRemoteModule } from '@angular-architects/native-federation'
import { AfterViewInit, Component, ViewChild, ViewContainerRef } from '@angular/core'

@Component({
  selector: 'app-tchat-window',
  standalone: true,
  template: `
    <h2>Dashboard du shell</h2>
    <div>
      <ng-container #remoteContainer></ng-container>
    </div>
  `,
})
export class TchatWindow implements AfterViewInit {
  @ViewChild('remoteContainer', { read: ViewContainerRef, static: true })
  remoteContainer!: ViewContainerRef

  ngAfterViewInit(): void {
    // Charge dynamiquement le composant exposé par users-app
    loadRemoteModule('users-app', './TchatWindow').then((remoteModule) => {
      console.log('Remote module loaded:', remoteModule)
      this.remoteContainer.clear()
      this.remoteContainer.createComponent(remoteModule.TchatWindow)
    })
  }
}
