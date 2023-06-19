package com.kunminx.purenote.domain.request;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;
import com.kunminx.purenote.data.repo.DataRepository;
import com.kunminx.purenote.domain.intent.NoteIntent;

/**
 * TODO tip 1：让 UI 和业务分离，让数据总是从生产者流向消费者
 * UI逻辑和业务逻辑，本质区别在于，前者是数据的消费者，后者是数据的生产者，
 * "领域层组件" 作为数据的生产者，职责应仅限于 "请求调度 和 结果分发"，
 * `
 * 换言之，"领域层组件" 中应当只关注数据的生成，而不关注数据的使用，
 * 改变 UI 状态的逻辑代码，只应在表现层页面中编写、在 Observer 回调中响应数据的变化，
 * 将来升级到 Jetpack Compose 更是如此，
 * <p>
 * Create by KunMinX at 2022/6/14
 */
public class NoteRequester extends MviDispatcher<NoteIntent> {

  /**
   * TODO tip 1：
   * 此为领域层组件，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   * 可在页面中配置作用域，以实现单页面独享或多页面数据共享，
   * `
   * 本组件通过封装，默使数据从 "领域层" 到 "表现层" 单向流动，
   * 消除 “mutable 样板代码 + 连发事件覆盖 + mutable.setValue 误用滥用” 等高频痛点。
   */
  @Override
  protected void onHandle(NoteIntent intent) {
    DataRepository repo = DataRepository.getInstance();
    switch (intent.id) {
      case NoteIntent.InitItem.ID:
        NoteIntent.InitItem initItem = (NoteIntent.InitItem) intent;
        sendResult(initItem.copy());
        break;
      case NoteIntent.GetNoteList.ID:
        NoteIntent.GetNoteList getNoteList = (NoteIntent.GetNoteList) intent;
        repo.getNotes().subscribe(notes -> sendResult(getNoteList.copy(notes)));
        break;
      case NoteIntent.UpdateItem.ID:
        NoteIntent.UpdateItem updateItem = (NoteIntent.UpdateItem) intent;
        repo.updateNote(updateItem.paramNote).subscribe(it -> sendResult(updateItem.copy(it)));
        break;
      case NoteIntent.MarkItem.ID:
        NoteIntent.MarkItem markItem = (NoteIntent.MarkItem) intent;
        repo.updateNote(markItem.paramNote).subscribe(it -> sendResult(markItem.copy(it)));
        break;
      case NoteIntent.ToppingItem.ID:
        NoteIntent.ToppingItem toppingItem = (NoteIntent.ToppingItem) intent;
        repo.updateNote(toppingItem.paramNote).subscribe(it ->
                repo.getNotes().subscribe(notes -> sendResult(NoteIntent.GetNoteList(notes))));
        break;
      case NoteIntent.AddItem.ID:
        NoteIntent.AddItem addItem = (NoteIntent.AddItem) intent;
        repo.insertNote(addItem.paramNote).subscribe(it -> sendResult(addItem.copy(it)));
        break;
      case NoteIntent.RemoveItem.ID:
        NoteIntent.RemoveItem removeItem = (NoteIntent.RemoveItem) intent;
        repo.deleteNote(removeItem.paramNote).subscribe(it -> sendResult(removeItem.copy(it)));
        break;
    }
  }
}
