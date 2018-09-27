import { Pipe, PipeTransform } from '@angular/core';
import {TaskanaDate} from '../../util/taskana.date';

@Pipe({
  name: 'datePipe'
})
export class DatePipe implements PipeTransform {
  transform(date: string, target: string) {
    if (date) {
      return target === 'ISO' ? TaskanaDate.getDateString(new Date(date), 'ISO') :
        TaskanaDate.getDateString(new Date(date), 'simple');
    }
    return '';
  }
}
