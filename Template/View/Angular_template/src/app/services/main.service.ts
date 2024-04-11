import { Injectable } from '@angular/core';
import axios from 'axios';

@Injectable({
  providedIn: 'root'
})
export class MainService {

  constructor() { }

  // TOUTE LES TABLES 
  voit:any[] = [];
  marque:any[] = [];

  // TOUTE LES FONCTIONS 
  // GET voiture axios
  async get_all_voiture(): Promise<any[]> {
    const url = 'http://localhost:3000/vehicule/getAll';
    try {
      const response: any = await axios.get(url);
      if (response && response.data) {
        console.log(response.data);
        this.voit = response.data;
        return this.voit;
      } else {
        return [];
      }
    } catch (error) {
      console.error('Erreur', error);
      return [];
    }
  }
  // GET marque axios
  async get_all_marque(): Promise<any[]> {
    const url = 'http://localhost:3000/marque/list';
    try {
      const response: any = await axios.get(url);
      if (response && response.data) {
        console.log(response.data);
        this.marque = response.data;
        return this.marque;
      } else {
        return [];
      }
    } catch (error) {
      console.error('Erreur', error);
      return [];
    }
  }
  // SAVE
  async save_voiture(matriculation: string | undefined,idmarque: number | undefined,nbPlace: number | undefined,poids: number | undefined): Promise<void> {
    const url = 'http://localhost:3000/vehicule/add';
    // LISTE ATTRIBUT A PASSER AU CONTROLLER
    var requestBodyData = {
      matriculation: matriculation,
      idmarque: idmarque,
      nbPlace: nbPlace,
      poids: poids
    }
    const response = await axios.post(url, requestBodyData);
    console.log(response)
  }
  // DELETE
  async delete_voiture(idvehicule: number | undefined): Promise<void> {
    const url = `http://localhost:3000/vehicule/clear?id=${idvehicule}`;
    const response = await axios.post(url);
    console.log(response)
  }



}
