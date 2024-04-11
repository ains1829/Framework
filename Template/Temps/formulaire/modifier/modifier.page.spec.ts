import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ModifierPage } from './modifier.page';

describe('ModifierPage', () => {
  let component: ModifierPage;
  let fixture: ComponentFixture<ModifierPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ModifierPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
