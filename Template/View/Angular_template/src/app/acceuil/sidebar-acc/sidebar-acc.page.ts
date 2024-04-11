import { Component, OnInit } from '@angular/core';
import { MainService } from 'src/app/services/main.service';

@Component({
  selector: 'app-sidebar-acc',
  templateUrl: './sidebar-acc.page.html',
  styleUrls: ['./sidebar-acc.page.scss'],
})
export class SidebarAccPage implements OnInit {

  constructor( private mainService: MainService,) { }

  ngOnInit() {
  }
  
  async logout(){
    await this.mainService.logout();
  }
}
