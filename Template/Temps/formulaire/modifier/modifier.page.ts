import { Component, Input, OnInit } from '@angular/core';
import { ModalController } from '@ionic/angular';
import { MainService } from 'src/app/services/main.service';
import { OverlayEventDetail } from '@ionic/core/components';

@Component({
  selector: 'app-modifier',
  templateUrl: './modifier.page.html',
  styleUrls: ['./modifier.page.scss'],
})
export class ModifierPage implements OnInit {
  // Props 
  @Input() item: any;
  data: any;
  isUpdateOpen = false;
  isUpdateAlertOpen = false;

  constructor(
    private mainService: MainService,
    private modalController: ModalController
  ) { }

  ngOnInit() {
  }

  
  // Fonction crud
  // exemple
  // async update#NomTable(nomAttr)
  async updateVoiture(){ // update
    console.log(this.item)
    // await this.mainService.update_voiture(this.item.idvehicule,this.item.matriculation,this.item.idmarque,this.item.nbPlace,this.item.poids);
  }
  setUpdateOpen(isOpen: boolean){
    this.isUpdateAlertOpen = isOpen
  }
// fin crud

  // Fonction utile pour l'affichage
  closePopup(){
    this.isUpdateOpen = false;
    this.modalController.dismiss();
  }
  cancel() {
    this.modalController.dismiss(null, 'cancel');
  }
  confirm() {
    this.modalController.dismiss(null, 'confirm');
  }
  // Update option
  public alertButtons = [
    {
      text: 'Cancel',
      role: 'cancel',
      handler: () => {
        console.log('Annuler');
      },
    },
    {
      text: 'OK',
      role: 'confirm',
      handler: () => {
        console.log('Confirmer');
      },
    },
  ];
  setResult(event: Event) {
    const ev = event as CustomEvent<OverlayEventDetail<string>>;
    this.isUpdateAlertOpen = false;
    console.log(this.item.idvehicule)
    console.log(`Dismissed with role: ${ev.detail.role}`);
    if( ev.detail.role == "confirm" ){
      this.updateVoiture(); // Except ::: si confirm donc appel fonction supprimer 
    }else if( ev.detail.role == "cancel"){
      // this.item = this.itemOrigin;
      this.closePopup();
    }
  }


}
