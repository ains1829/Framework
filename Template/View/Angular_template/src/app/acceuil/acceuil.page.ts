import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-acceuil',
  templateUrl: './acceuil.page.html',
  styleUrls: ['./acceuil.page.scss'],
})
export class AcceuilPage implements OnInit {

  constructor(
    private router:Router
  ) { }

  ngOnInit() {
  }

  authentification_page(){
    this.router.navigate(['login']);
  }

}
