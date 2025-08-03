import { loadRemoteModule } from '@angular-architects/native-federation';
import { AfterViewInit, Component, ViewChild, ViewContainerRef } from '@angular/core';

@Component({
  selector: 'app-user-view',
  standalone: true,
  template: `
    <h2>Dashboard du shell</h2>
    <div>
      <ng-container #remoteContainer></ng-container>
    </div>
  `
})
export class UserView implements AfterViewInit {
  @ViewChild('remoteContainer', { read: ViewContainerRef, static: true })
  remoteContainer!: ViewContainerRef;

  ngAfterViewInit(): void {
    // Charge dynamiquement le composant exposé par users-app
    loadRemoteModule('users-app', './UserView').then(remoteModule => {
      console.log('Remote module loaded:', remoteModule);
      this.remoteContainer.clear();
      this.remoteContainer.createComponent(remoteModule.UserView);
    });
  }
}
