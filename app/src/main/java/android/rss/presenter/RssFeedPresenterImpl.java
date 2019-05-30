package android.rss.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.rss.model.RssFeedModel;
import android.rss.model.task.Task;
import android.rss.view.RssFeedView;

public class RssFeedPresenterImpl implements RssFeedPresenter {

    private RssFeedView view;

    private Task loadRssTask;
    private Task createDbTask;

    public RssFeedPresenterImpl(RssFeedView view, Task loadRssTask, Task createDbTask) {
        this.view = view;
        this.loadRssTask = loadRssTask;
        this.createDbTask = createDbTask;
    }

    @Override
    public void startLoadRss() {
        loadRssTask.start(resultHandler);

        createDbTask.start(null);
    }

    @Override
    public void stopLoadRss() {
        loadRssTask.stop();

        createDbTask.stop();
    }

    // Handler создается в Главном потоке
    private Handler resultHandler = new Handler(Looper.myLooper()) {

        @Override
        public void handleMessage(Message message) {
            // Получаем пакет из Сообщения
            Bundle bundle = message.getData();

            // Из пакета получаем Rss каналы
            RssFeedModel rssFeedModel = (RssFeedModel) bundle.getSerializable(Task.DATA_KEY);

            // TODO Сохранение модели данных в БД, через Gson (конвертинг в Json)

            // Показываем базовые данные канала
            view.showRss(rssFeedModel.getTitle());

            // Показываем каналы
            for (int i = 0; i < rssFeedModel.getItemsSize(); i++) {
                view.showRss(rssFeedModel.getItem(i).getTitle());
            }
        }
    };
}
