//
//  TableCustomCell.m
//  TwitterBoard
//
//  Created by GLB-254 on 4/18/15.
//   All rights reserved.
//

#import "TableCustomCellFboard.h"
#import "UIImageView+WebCache.h"
@implementation TableCustomCellFboard

- (void)awakeFromNib {
    // Initialization code
}
- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if(self)
    {
        if([reuseIdentifier isEqualToString:@"Follow"])
        {
            self.userImage=[[UIImageView alloc]init];
            self.userImage.frame=CGRectMake(20, 20, 50, 50);
    
            // self.userImage.frame=CGRectMake(300, 20, 50, 50);
            self.userImage.layer.cornerRadius=10;
            self.userImage.layer.masksToBounds = YES;
            [self.contentView addSubview:self.userImage];
            
            self.fromUserImage=[[UIImageView alloc]init];
             self.fromUserImage.userInteractionEnabled = YES;
            self.fromUserImage.frame=CGRectMake(0, 0, 30, 30);
            
            self.fromUserImage.layer.cornerRadius=self.fromUserImage.frame.size.width/2;
            self.fromUserImage.layer.masksToBounds = YES;
            [self.contentView addSubview:self.fromUserImage];
            
            self.userNameDesc=[[UILabel alloc]init];
            self.userNameDesc.text=@"My feeds";
            self.userNameDesc.numberOfLines = 0;
            self.userNameDesc.font=[UIFont fontWithName:@"Helvetica-Bold" size:12];
            self.userNameDesc.frame=CGRectMake(100, 0, 220, 100);
            [self.contentView addSubview:self.userNameDesc];
            
            
            self.likeCountLabel = [[UILabel alloc]init];
            self.likeCountLabel.text=@"Like count=10";
            self.likeCountLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:10];
            self.likeCountLabel.frame=CGRectMake(20, 230, 100, 50);
         //  [self.containerView addSubview:self.likeCountLabel];
            
            self.commentCountLabel = [[UILabel alloc]init];
            self.commentCountLabel.text=@"comment count=10";
            self.commentCountLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:10];
            self.commentCountLabel.frame=CGRectMake(120,50, 100, 50);
             [self.contentView addSubview:self.commentCountLabel];
            
        }
        if([reuseIdentifier isEqualToString:@"Display"])
        {
            self.contentView.backgroundColor = [UIColor clearColor];
            self.containerView = [[UIView alloc]initWithFrame:CGRectMake(5, 5, [UIScreen mainScreen].bounds.size.width-10, 110)];
            self.containerView.backgroundColor = [UIColor whiteColor];
            self.containerView.layer.cornerRadius = 5;
            self.containerView.clipsToBounds = YES;
            [self.contentView addSubview:self.containerView];
            
            UIBezierPath *shadowPath = [UIBezierPath bezierPathWithRect:self.containerView.bounds];
            self.containerView.layer.masksToBounds = NO;
            self.containerView.layer.shadowColor = [UIColor grayColor].CGColor;
            self.containerView.layer.shadowOffset = CGSizeZero;
            self.containerView.layer.shadowOpacity = 1;
            self.containerView.layer.shadowPath = shadowPath.CGPath;
            
            self.userImage=[[UIImageView alloc]init];
            self.userImage.frame=CGRectMake(10, 20, 70, 70);
            self.userImage.contentMode=UIViewContentModeScaleAspectFit;
                         //   self.userImage.layer.cornerRadius=20;
            [self.containerView addSubview:self.userImage];
            
            self.fromUserImage=[[UIImageView alloc]init];
            self.fromUserImage.userInteractionEnabled = YES;
            self.fromUserImage.frame=CGRectMake(0, 0, 30, 30);
                //            self.fromUserImage.layer.cornerRadius=20;
                //            self.fromUserImage.layer.masksToBounds = YES;
            [self.containerView addSubview:self.fromUserImage];
            
            
            
            self.userNameDesc=[[UILabel alloc]init];
            self.userNameDesc.text=@"My feeds";
            self.userNameDesc.numberOfLines = 0;
            self.userNameDesc.font=[UIFont fontWithName:@"Baskerville-Bold" size:15];
            self.userNameDesc.frame=CGRectMake(self.userImage.frame.size.width+self.userImage.frame.origin.x+10, 0, 220,50);
            [self.containerView addSubview:self.userNameDesc];
            
            
            
            
            self.fromLabel=[[UILabel alloc]init];
            self.fromLabel.numberOfLines = 1;
            self.fromLabel.frame=CGRectMake(self.userImage.frame.size.width+self.userImage.frame.origin.x+10,40, 300, 40);
                        //self.fromLabel.textColor = [UIColor colorWithRed:(CGFloat)97/255 green:(CGFloat)197/255 blue:(CGFloat)231/255 alpha:1];
            self.fromLabel.textColor=[UIColor grayColor];
           // self.fromLabel.font=[UIFont fontWithName:@"Baskerville-Bold" size:5];
            self.fromLabel.font=[UIFont systemFontOfSize:12];
            
            [self.containerView addSubview:self.fromLabel];
            
            
            
            
            self.likeCountLabel = [[UILabel alloc]init];
            self.likeCountLabel.text=@"Like count=10";
            self.likeCountLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:10];
            self.likeCountLabel.hidden=YES;
            self.likeCountLabel.frame=CGRectMake(90,50, 100, 50);
             [self.contentView addSubview:self.likeCountLabel];
            //-----------------
            self.commentCountLabel = [[UILabel alloc]init];
            self.commentCountLabel.text=@"comment count=10";
            self.commentCountLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:10];
            self.commentCountLabel.hidden=YES;
            self.commentCountLabel.frame=CGRectMake(120, 100, 100, 50);
           [self.contentView addSubview:self.commentCountLabel];
        }
        
        if([reuseIdentifier isEqualToString:@"Myfeed"])
        {
            self.userImage=[[UIImageView alloc]init];
            self.userImage.frame=CGRectMake(20, 100, 80, 80);
                // self.userImage.frame=CGRectMake(300, 20, 50, 50);
          //  self.userImage.layer.cornerRadius=20;
            self.userImage.layer.masksToBounds = YES;
            [self.contentView addSubview:self.userImage];
            
            self.fromUserImage=[[UIImageView alloc]init];
            self.fromUserImage.userInteractionEnabled = YES;
            self.fromUserImage.frame=CGRectMake(10, 10, 50, 50);
            self.fromUserImage.layer.cornerRadius=self.fromUserImage.frame.size.width/2;
//            self.fromUserImage.image = [UIImage imageNamed:@"hel.jpeg"];
            [self.contentView addSubview:self.fromUserImage];
            
            self.userNameLabel=[[UILabel alloc]init];
//            self.userNameLabel.text=@"Vinayaka S Yattinahalli";
                //self.userNameLabel.numberOfLines = 0;
            self.userNameLabel.font=[UIFont fontWithName:@"Helvetica-Bold" size:15];
            self.userNameLabel.frame=CGRectMake(67, 0, 250, 40);
            [self.contentView addSubview:self.userNameLabel];
            
            self.timeLabel=[[UILabel alloc]init];
//            self.timeLabel.text=@"12:12:12:12 UTC";
            self.timeLabel.numberOfLines = 0;
            [self.timeLabel setTextColor:[UIColor grayColor]];
            self.timeLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:12];
            self.timeLabel.frame=CGRectMake(67, 21, 160, 40);
            [self.contentView addSubview:self.timeLabel];
            
            
            self.userNameDesc=[[UILabel alloc]init];
            self.userNameDesc.text=@"My feeds";
            self.userNameDesc.numberOfLines = 0;
            self.userNameDesc.font=[UIFont fontWithName:@"HelveticaNeue" size:15];
           self.userNameDesc.frame=CGRectMake(10, 60,SCREEN_WIDTH-80, 40);
            [self.contentView addSubview:self.userNameDesc];
            
            
            
           
            
//            UIBarButtonItem *commn = [[UIBarButtonItem alloc]  initWithImage:[UIImage imageNamed:@"place.png"] style:UIBarButtonItemStylePlain target:self action:@selector(addLocation)];
//            
//            [barItems addObject:location];

            
        }
       if([reuseIdentifier isEqualToString:@"Display1"])
        {
            
            self.contentView.backgroundColor = [UIColor clearColor];
            
            
            
            
            self.userImage=[[UIImageView alloc]init];
            self.userImage.frame=CGRectMake(20, 40,40, 40);
            self.userImage.contentMode=UIViewContentModeScaleAspectFit;
                //            self.userImage.layer.cornerRadius=20;
            [self.contentView addSubview:self.userImage];
            
            self.fromUserImage=[[UIImageView alloc]init];
            self.fromUserImage.userInteractionEnabled = YES;
            self.fromUserImage.frame=CGRectMake(5, 5, 30, 30);
                            self.fromUserImage.layer.cornerRadius=15;
                            self.fromUserImage.layer.masksToBounds = YES;
            [self.contentView addSubview:self.fromUserImage];
            
            self.fromLabel=[[UILabel alloc]init];
            self.fromLabel.numberOfLines = 0;
            self.fromLabel.frame=CGRectMake(50, 0, 200, 40);
                //            self.fromLabel.textColor = [UIColor blueColor];
            
            self.fromLabel.contentMode=UIViewContentModeScaleAspectFit;
            self.fromLabel.font=[UIFont fontWithName:@"Baskerville-Bold" size:20];
            [self.contentView addSubview:self.fromLabel];
            
            
            self.userNameDesc=[[UILabel alloc]init];
            self.userNameDesc.text=@"My feeds";
            self.userNameDesc.numberOfLines = 0;
          //  self.userNameDesc.backgroundColor=[UIColor redColor];
            self.userNameDesc.font=[UIFont fontWithName:@"AvenirNextCondensed-Medium" size:12];
            self.userNameDesc.frame=CGRectMake(75, 40, self.frame.size.width-100,80);
            [self.contentView addSubview:self.userNameDesc];
            
            
            self.likeCountLabel = [[UILabel alloc]init];
            self.likeCountLabel.text=@"Like count=10";
            self.likeCountLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:8];
            self.likeCountLabel.frame=CGRectMake(20, 100, 100, 50);
            self.likeCountLabel.textColor=[UIColor blackColor];
            [self.containerView addSubview:self.likeCountLabel];
            
            self.commentCountLabel = [[UILabel alloc]init];
            self.commentCountLabel.text=@"comment count=10";
            self.commentCountLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:10];
            self.commentCountLabel.frame=CGRectMake(120, 230, 100, 50);
            [self.containerView addSubview:self.commentCountLabel];
            
            self.timeLabel=[[UILabel alloc]init];
            //            self.timeLabel.text=@"12:12:12:12 UTC";
            self.timeLabel.numberOfLines = 0;
            [self.timeLabel setTextColor:[UIColor grayColor]];
            self.timeLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:12];
            self.timeLabel.frame=CGRectMake(80, 18, 160, 40);
            [self.contentView addSubview:self.timeLabel];
              }
        
    //#
#pragma mark search
       else if ([reuseIdentifier isEqualToString:@"SearchingIdentifier"])
       {
           
           //Addiing Container view for shadow
           self.contentView.backgroundColor = [UIColor clearColor];
           self.containerView = [[UIView alloc]initWithFrame:CGRectMake(0,0, [UIScreen mainScreen].bounds.size.width-10, 110)];
           self.containerView.backgroundColor = [UIColor whiteColor];
           self.containerView.layer.cornerRadius = 5;
           self.containerView.clipsToBounds = YES;
           [self.contentView addSubview:self.containerView];
          //----------
           self.containerView.layer.masksToBounds = NO;
           self.containerView.layer.shadowOffset = CGSizeMake(0,0);
           self.containerView.layer.shadowRadius = 5;
           self.containerView.layer.shadowOpacity = 0.5;
           //-----------
           self.userImage=[[UIImageView alloc]init];
           self.userImage.frame=CGRectMake(20, 40,40, 40);
           self.userImage.contentMode=UIViewContentModeScaleAspectFit;
           //            self.userImage.layer.cornerRadius=20;
           [self.contentView addSubview:self.userImage];
           //----------
           self.headingTop=[[UILabel alloc]init];
           self.headingTop.frame=CGRectMake(80, 5, SCREEN_WIDTH-80, 40);
           self.headingTop.font=[UIFont systemFontOfSize:15];
           self.headingTop.numberOfLines=0;
           self.headingTop.lineBreakMode=NSLineBreakByWordWrapping;
           [self.containerView addSubview:self.headingTop];
           //-----
           self.descriptionHeadingLbl=[[UILabel alloc]init];
           self.descriptionHeadingLbl.frame=CGRectMake(80,45, SCREEN_WIDTH-80,20);
           self.descriptionHeadingLbl.font=[UIFont systemFontOfSize:12];
           [self.containerView addSubview:self.descriptionHeadingLbl];
           //-----------
           self.placeLabel=[[UILabel alloc]init];
           self.placeLabel.frame=CGRectMake(80, 65, SCREEN_WIDTH-80,15);
           self.placeLabel.font=[UIFont systemFontOfSize:12];
           [self.containerView addSubview:self.placeLabel];
           //---------
          self.likeCountLabel=[[UILabel alloc]init];
           self.likeCountLabel.frame=CGRectMake(80,80, SCREEN_WIDTH-80,15);
           self.likeCountLabel.font=[UIFont systemFontOfSize:12];
           [self.containerView addSubview:self.likeCountLabel];
           //-------
           self.peopleWhereLabel=[[UILabel alloc]init];
           self.peopleWhereLabel.frame=CGRectMake(80,100, SCREEN_WIDTH-80, 15);
           self.peopleWhereLabel.font=[UIFont systemFontOfSize:12];
           [self.containerView addSubview:self.peopleWhereLabel];
           //----------

       }
        
        
        
   //
        
        
        

    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
