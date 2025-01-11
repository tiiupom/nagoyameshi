 const imageInput = document.getElementById('imageFile');
 const imagePreview = document.getElementById('imagePreview');
 
 imageInput.addEventListener('change', () => {
   if (imageInput.files[0]) {
     let fileReader = new FileReader();
     fileReader.onload = () => {
       imagePreview.innerHTML = `<img src="${fileReader.result}" class="mb-3">`;
     }
     fileReader.readAsDataURL(imageInput.files[0]);
   } else {
     imagePreview.innerHTML = '';
   }
 })

 
/*
1.idにimageFileが指定されているHTML要素(input要素)の値が変更されるたびイベント処理が実行
2.画像ファイルが正常に読み込まれたらidに"imagePreview"が指定されているHTML要素の中に
  読み込まれた画像ファイルを表示するためのimg要素を挿入
3.選択された画像ファイルがない場合idにimagePreviewが指定されているHTML要素の中身を空にする
*/