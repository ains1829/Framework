import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoadingController } from '@ionic/angular';
import { MainService } from 'src/app/services/main.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
})
export class LoginPage implements OnInit {
//loading
private loading:any;

// Attributs
  username!: string;
  userpassword!: string; 
  erreur!: string;

  constructor(
    private router:Router,
    private mainService: MainService, 
    private loadingCtrl: LoadingController
  ) { }

  ngOnInit() {
  }

  // fonction login 
  async login(){
    await this.loadingCtrl.create({
      message: "Connexion..."
    }).then((overlay) => {
      this.loading = overlay;
      this.loading.present();
    });
    console.log("Identifiant :" + this.username + ":" + this.userpassword);
    const reps = await this.mainService.authentification(this.username,this.userpassword);
    await this.loading.dismiss();
    console.log(reps);
    if( reps.status === "000" ){
      this.erreur = reps.message;
    }else if( reps.status === "200" ){
      localStorage.setItem('iduser' , reps.id) ;
      this.router.navigate(['/acceuil/home']);
    }
  }

}
