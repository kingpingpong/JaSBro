package jasbro.game.world.customContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.java.truevfs.access.TFile;

public class CustomQuestTemplate implements Serializable {
    private String id;    
    private List<CustomQuestStage> questStages;
    private transient TFile file;
    
    public CustomQuestTemplate(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<CustomQuestStage> getQuestStages() {
        if (questStages == null) {
            questStages = new ArrayList<CustomQuestStage>();
        }
        return questStages;
    }
    public void setQuestStages(List<CustomQuestStage> questStages) {
        this.questStages = questStages;
    }
    public TFile getFile() {
        return file;
    }
    public void setFile(TFile file) {
        this.file = file;
    }
    
    @Override
    public String toString() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CustomQuestTemplate other = (CustomQuestTemplate) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
