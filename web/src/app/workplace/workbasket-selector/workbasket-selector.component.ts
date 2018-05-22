import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Task} from 'app/workplace/models/task';
import {Workbasket} from 'app/models/workbasket';
import {TaskService} from 'app/workplace/services/task.service';
import {WorkbasketService} from 'app/services/workbasket/workbasket.service';

@Component({
  selector: 'taskana-workbasket-selector',
  templateUrl: './workbasket-selector.component.html',
  styleUrls: ['./workbasket-selector.component.scss']
})
export class SelectorComponent implements OnInit {

  @Output()
  tasksChanged = new EventEmitter<Task[]>();

  tasks: Task[] = [];
  autoCompleteData: string[] = [];
  result = '';
  resultId: string;
  workbaskets: Workbasket[];
  currentBasket: Workbasket;

  constructor(private taskService: TaskService,
              private workbasketService: WorkbasketService) {
  }

  ngOnInit() {
    this.workbasketService.getAllWorkBaskets().subscribe(workbaskets => {
      this.workbaskets = workbaskets._embedded ? workbaskets._embedded.workbaskets : [];
      this.workbaskets.forEach(workbasket => {
        this.autoCompleteData.push(workbasket.name);
      });
    });
  }

  searchBasket() {
    if (this.workbaskets) {
      this.workbaskets.forEach(workbasket => {
        if (workbasket.name === this.result) {
          this.resultId = workbasket.workbasketId;
          this.currentBasket = workbasket;
        }
      });
      this.getTasks(this.resultId);
      this.tasksChanged.emit(this.tasks);
    }
  }

  getTasks(workbasketId: string) {
    this.taskService.findTasksWithWorkbasket(workbasketId).subscribe(
      tasks => {
        if (!tasks || tasks._embedded === undefined) {
          this.tasks.length = 0;
          return;
        }
        tasks._embedded.tasks.forEach(e => this.tasks.push(e));
      });
  }

  isDisabled(): boolean {
    return (this.currentBasket === undefined ? (this.result.length === 0) : (this.currentBasket.name === this.result) || (this.result.length === 0));
  }
}
