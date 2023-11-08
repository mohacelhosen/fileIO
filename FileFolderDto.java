package lynorg.filemngt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileFolderDto{
    private String contentFileAbsolutPath;
    private ArrayList<FolderFile> listFolderFile;
}