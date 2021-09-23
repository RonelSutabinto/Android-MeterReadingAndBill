package com.zaneco.readandbill;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import com.generic.readandbill.database.Consumer;
import com.generic.readandbill.database.Reading;
import com.zaneco.readandbill.database.ReadingDataSource;

public class MyConsumerList extends com.generic.readandbill.MyConsumerList {

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        ReadingDataSource dsReading = new ReadingDataSource(this);
        Reading reading = dsReading.getReading(adapter.getItemId(info.position),ReadingDataSource.CONSUMER_ID);
        menu.setHeaderTitle("Consumer Transaction Options");
        if (reading.getId() == -1)
            menu.add(0, FIELDFINDING_ID, 0, "Field Finding Entry");
        else
            menu.add(0, DELETE_ID, 0, "Delete Entry");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        switch (item.getItemId()) {
            case FIELDFINDING_ID:
                Intent intent = new Intent(this, MyFieldFindingEntry.class);
                intent.putExtra("id", adapter.getItemId(info.position));
                intent.putExtra("pos", info.position);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case DELETE_ID:
                Consumer consumer =  adapter.getItem(info.position);
                dsr.deleteReading(dsr.getReading(consumer.getId(),
                        ReadingDataSource.CONSUMER_ID));
                adapter.remove(adapter.getItem(info.position));
                adapter.insert(consumer, info.position);
                applySort();
            default:
                break;
        }
        return true;
    }

    @Override
    protected Intent myTransactionActivityCaller(Reading reading) {
        Intent intent = null;
        if (reading.getId() != -1){
            if (reading.getFieldFinding() != -1){
                intent = new Intent(this, MyFieldFindingEntry.class);
            } else {
                intent = new Intent(this, MyReadingEntry.class);
            }
        } else {
            intent = new Intent(this, MyReadingEntry.class);
        }
        return intent;
    }

}
