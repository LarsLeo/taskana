import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'taskana-classification-list',
  templateUrl: './classification-list.component.html',
  styleUrls: ['./classification-list.component.scss']
})
export class ClassificationListComponent implements OnInit {

  requestInProgress = false;

  constructor() {
  }

  ngOnInit() {
  }
}
