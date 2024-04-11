import { Component, OnInit } from '@angular/core';
import { MainService } from '../../../services/main.service';


@Component({
  selector: 'app-insert',
  templateUrl: './insert.page.html',
  styleUrls: ['./insert.page.scss'],
})
export class InsertPage implements OnInit {
  // Liste des attributs 
  idvehicule: number | undefined; 
  matriculation: string | undefined;
  idmarque: number | undefined;
  nbPlace: number | undefined;
  poids: number | undefined;

  constructor(private mainService: MainService) { }

  ngOnInit() {
    
  }

  // Fonction crud insertion 
  // async save#NomTable()
  async saveVoiture(){
    // appel fonction coresspondante(attr1,attr2,...)
    await this.mainService.save_voiture(this.matriculation,this.idmarque,this.nbPlace,this.poids);
  }

}
