package lynorg.filemngt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderFile {
    private String folderPath;
    private String newFileName;
}
