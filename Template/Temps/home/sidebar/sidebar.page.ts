import { Component, OnInit } from '@angular/core';
import { MainService } from 'src/app/services/main.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.page.html',
  styleUrls: ['./sidebar.page.scss'],
})
export class SidebarPage implements OnInit {

  constructor(
    private mainService: MainService,
  ) { }

  ngOnInit() {
  }


  // Logout
  async logout(){
    await this.mainService.logout();
  }

}
