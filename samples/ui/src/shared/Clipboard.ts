const TYPE = 'text/plain'

export class Clipboards {

  static async copy(data: string) {
    const blob = new Blob([data], {type: TYPE});

    const cbi = new ClipboardItem({
      [TYPE]: blob,
    });
    await (navigator.clipboard as Clipboard).write([cbi]);
  }

}
